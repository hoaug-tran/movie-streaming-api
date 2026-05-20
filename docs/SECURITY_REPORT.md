# Backend Security Report

## Phạm vi

Tài liệu này tổng hợp các cơ chế bảo mật hiện có trong backend `movie-streaming-api`.

Phạm vi kiểm tra gồm:

- Cấu hình Spring Security.
- Xác thực và quản lý token.
- Phân quyền theo role và endpoint.
- Rate limiting và chống abuse.
- Bảo mật streaming, HLS key, offline download.
- Bảo mật upload media.
- Bảo mật thanh toán PayOS và webhook.
- Bảo mật chatbot AI.
- CORS, cookie, cấu hình secret, logging và vận hành.
- Các giới hạn còn tồn tại và khuyến nghị cải tiến.

## Tổng quan kiến trúc bảo mật

Backend sử dụng Spring Boot với mô hình API stateless:

- Spring Security bảo vệ request ở tầng filter chain.
- JWT access token xác thực user cho API protected.
- Refresh token lưu phía backend để có thể revoke.
- Cookie `HttpOnly` dùng để lưu token cho client web.
- Method security với `@PreAuthorize` dùng cho các tác vụ cần quyền cao.
- Rate limit interceptor chặn spam, brute force và abuse theo nhóm endpoint.
- Media streaming dùng HLS AES-128, key nằm ngoài public storage.
- Offline download dùng token ký số có TTL 48 giờ.
- Payment webhook dùng xác minh chữ ký/checksum từ PayOS SDK.

## Cấu hình Spring Security

File chính:

- `src/main/java/com/hoaug/movieapi/config/SecurityConfig.java`

Các cấu hình chính:

```java
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig
```

Backend bật:

- `@EnableWebSecurity` cho web security.
- `@EnableMethodSecurity` cho `@PreAuthorize`.
- `SessionCreationPolicy.STATELESS` để không dùng server session.
- JWT filter chạy trước `UsernamePasswordAuthenticationFilter`.
- CORS lấy từ cấu hình môi trường.
- BCrypt làm password encoder.

Các quyết định bảo mật quan trọng:

| Cơ chế | Trạng thái | Mục đích |
|---|---|---|
| Stateless session | Có | Tránh session server state, phù hợp JWT API |
| JWT filter | Có | Xác thực mỗi request bằng token |
| Method security | Có | Bảo vệ endpoint admin/service nhạy cảm |
| BCryptPasswordEncoder | Có | Hash password và OTP |
| CORS allow origins | Có cấu hình | Chỉ cho origin được khai báo |
| CSRF | Tắt | API stateless, token-based |

## Ma trận endpoint public và protected

### Public endpoint

Các endpoint public được mở có chủ đích:

| Endpoint | Method | Lý do public |
|---|---:|---|
| `/api/v1/auth/**` | All | Đăng ký, đăng nhập, OTP, reset password |
| `/api/v1/movies/**` | GET | Cho phép xem danh sách và chi tiết phim |
| `/api/v1/movies/*/view` | POST | Ghi nhận lượt xem public |
| `/api/v1/movies/search` | POST | Tìm kiếm phim public |
| `/api/v1/movies/search/advanced` | POST | Tìm kiếm nâng cao public |
| `/api/v1/movies/categories` | GET | Danh mục phim public |
| `/api/v1/subscription-plans/**` | All | Xem gói subscription |
| `/api/v1/subscriptions/plans` | GET | Xem gói subscription |
| `/api/v1/payments/success` | All | Callback success |
| `/api/v1/advertisements/active` | GET | Lấy quảng cáo active |
| `/api/v1/advertisements/type/**` | GET | Lấy quảng cáo theo loại |
| `/api/v1/ads/**` | All | Ads API public |
| `/api/v1/webhooks/**` | All | Webhook provider gọi vào |
| `/api/v1/search-histories/search` | GET | Search suggestion public |
| `/api/v1/discovery/**` | All | Discovery public |
| `/actuator/health` | All | Health check |
| `/api/v1/system/status` | GET | Trạng thái hệ thống public |
| `/api/v1/support/contact` | POST | Form liên hệ public |
| `/avatar/**` | GET | Avatar static public |
| `/swagger-ui/**`, `/v3/api-docs/**`, `/swagger-resources/**` | All | API docs |
| `/api/v1/comments/movie/**` | GET | Đọc bình luận public |
| `/api/v1/reviews/movie/**` | GET | Đọc review public |
| `/api/v1/stream/keys/**` | GET | Key endpoint tự kiểm tra quyền bên trong use case/controller |
| `/api/v1/stream/offline/key/**` | GET | Offline key kiểm tra offline token |

### Protected endpoint

Các endpoint yêu cầu xác thực:

| Endpoint | Quyền |
|---|---|
| `/api/v1/auth/me` | Authenticated |
| `/api/v1/chat/**` | Authenticated |
| `/api/v1/users/**` | Authenticated |
| `/api/v1/watch-histories/**` | Authenticated |
| `/api/v1/watchlists/**` | Authenticated |
| `/api/v1/favorites/**` | Authenticated |
| `/api/v1/comments/**` ngoài GET movie | Authenticated |
| `/api/v1/reviews/**` ngoài GET movie | Authenticated |
| `/api/v1/payments/**` | Authenticated |
| `/api/v1/subscriptions/**` | Authenticated |
| `/api/v1/notifications/**` | Authenticated |
| `/api/v1/device-sessions/**` | Authenticated |
| `/api/v1/recommendations/**` | Authenticated |
| `/api/v1/stream/offline/**` | Authenticated |
| `/api/v1/stream/sessions/**` | Authenticated |
| `/api/v1/push/**` | Authenticated |
| `/api/v1/search-histories/**` | Authenticated |
| Default `.anyRequest()` | Authenticated |

### Admin endpoint

| Endpoint | Quyền |
|---|---|
| `/api/v1/admin/**` | `ROLE_ADMIN` |
| `/api/v1/chat/health` | `ROLE_ADMIN` |
| Admin controller methods có `@PreAuthorize("hasRole('ADMIN')")` | `ROLE_ADMIN` |

## Xác thực JWT

File chính:

- `src/main/java/com/hoaug/movieapi/modules/auth/infrastructure/security/JwtService.java`
- `src/main/java/com/hoaug/movieapi/modules/auth/infrastructure/security/JwtAuthenticationFilter.java`

JWT được ký bằng HMAC key lấy từ cấu hình:

```properties
jwt.secret-key=${JWT_SECRET_KEY:}
jwt.access-token-expiration=${JWT_EXPIRATION_MS:86400000}
```

Cơ chế hiện tại:

- Access token sinh bằng subject là username/email.
- Token có `issuedAt` và `expiration`.
- Token được verify bằng signing key server.
- Nếu secret không cấu hình, service ném lỗi khi khởi tạo.
- Refresh token không phải JWT, được tạo từ `SecureRandom` 64 byte và encode Base64 URL-safe.

Điểm mạnh:

- Không dùng predictable refresh token.
- Access token có thời hạn.
- Secret đọc từ environment trong production.
- Token verification dùng thư viện `jjwt` thay vì tự parse thủ công.

Điểm cần chú ý:

- `application-local.properties` có secret local phục vụ dev. Không dùng file local này cho production.
- Thời hạn access token mặc định hiện là `86400000` ms, tức 24 giờ. Có thể giảm xuống 15 đến 60 phút nếu muốn siết production.

## Cookie authentication

File chính:

- `src/main/java/com/hoaug/movieapi/common/util/CookieUtil.java`

Cookie hiện có:

| Cookie | Max age | Mục đích |
|---|---:|---|
| `accessToken` | 1 giờ | Xác thực API nhanh |
| `refreshToken` | 30 ngày | Cấp access token mới |
| `trusted_device` | Theo use case OTP | Ghi nhận thiết bị tin cậy |

Cấu hình cookie:

```properties
app.cookie.secure=${APP_COOKIE_SECURE:false}
app.cookie.same-site=${APP_COOKIE_SAME_SITE:Lax}
app.cookie.domain=${APP_COOKIE_DOMAIN:}
```

Thuộc tính bảo mật:

- `HttpOnly=true`, JavaScript frontend không đọc được cookie.
- `Secure` bật bằng cấu hình môi trường.
- `SameSite` bật bằng cấu hình môi trường.
- `path=/`.
- Có cơ chế clear cookie khi logout.

Khuyến nghị production:

```properties
APP_COOKIE_SECURE=true
APP_COOKIE_SAME_SITE=Lax
APP_COOKIE_DOMAIN=.giophim.libsys.me
```

Nếu frontend và backend khác site, cần cân nhắc `SameSite=None` kèm `Secure=true`.

## Password, OTP và reset password

Backend dùng `BCryptPasswordEncoder` cho password và các OTP hash.

Các use case liên quan:

- Đăng ký và xác thực OTP đăng ký.
- Đăng nhập và xác thực OTP nếu cần.
- Quên mật khẩu và reset password bằng OTP.
- Đổi mật khẩu sau khi xác thực user.
- Đổi email với OTP cho email hiện tại và email mới.

Điểm bảo mật:

- Password không lưu plaintext.
- OTP được hash trước khi lưu vào session/cache/entity liên quan.
- Đổi mật khẩu revoke refresh token hiện có của user.
- Login có hỗ trợ trusted device để giảm OTP lặp lại trên thiết bị tin cậy.

## Refresh token và logout

Refresh token được lưu server-side trong database qua repository riêng.

Cơ chế:

- Refresh token sinh bằng `SecureRandom`.
- Refresh token có expiry trong entity/domain.
- Logout revoke token hiện tại.
- Đổi mật khẩu revoke refresh token của user.
- Có repository cleanup token hết hạn.

Lợi ích:

- Server có thể vô hiệu hóa refresh token.
- Token bị lộ có thể bị revoke khi logout hoặc đổi mật khẩu.
- Không phụ thuộc hoàn toàn vào expiry của JWT access token.

## Phân quyền role-based access control

Backend sử dụng cả endpoint matcher và method-level authorization.

Cơ chế:

- `/api/v1/admin/**` yêu cầu `ROLE_ADMIN`.
- Nhiều controller admin dùng `@PreAuthorize("hasRole('ADMIN')")`.
- User endpoint yêu cầu authenticated.
- Chat health yêu cầu admin.
- Chat stream yêu cầu authenticated.

Ví dụ class có method security:

- `UserController`
- `SubscriptionController`
- `AdminChunkedUploadController`
- `ChatController`

Điểm mạnh:

- Có bảo vệ ở nhiều lớp.
- Default rule là authenticated, tránh quên cấu hình endpoint mới.
- Các API admin nhạy cảm được role-gate rõ ràng.

## Rate limiting và chống abuse

File chính:

- `src/main/java/com/hoaug/movieapi/common/security/RateLimiter.java`
- `src/main/java/com/hoaug/movieapi/common/security/RateLimitInterceptor.java`
- `src/main/java/com/hoaug/movieapi/config/RateLimitConfig.java`

Rate limiting hiện theo fixed window có cleanup key cũ.

Các policy hiện tại:

| Nhóm endpoint | Limit | Window | Actor |
|---|---:|---:|---|
| `/auth/login` | 5 | 10 phút | IP |
| `/auth/register`, `/auth/forgot-password`, `/auth/reset-password`, `/auth/otp` | 6 | 10 phút | IP |
| `/chat/stream` | 20 | 1 giờ | User nếu có, fallback IP |
| `/stream/offline`, `/stream/sessions` | 30 | 1 phút | User nếu có, fallback IP |
| `/support/contact` | 3 | 10 phút | IP |
| `/advertisements/views` | 60 | 1 phút | IP |
| Non-GET write API | 60 | 1 phút | User nếu có, fallback IP |
| Reviews/comments read | 120 | 1 phút | IP |
| Movie search | 80 | 1 phút | IP |
| API read default | 240 | 1 phút | IP |

IP resolution ưu tiên:

1. `CF-Connecting-IP`
2. `X-Forwarded-For` IP đầu tiên
3. `request.getRemoteAddr()`

Endpoint được skip:

- OAuth callback path có `/auth/oauth/`.
- `OPTIONS` preflight.
- `/api/v1/stream/keys/` để không làm nghẽn HLS key playback.

Điểm mạnh:

- Login, reset password và OTP được siết riêng.
- AI chat stream bị giới hạn để tránh tốn tài nguyên model.
- Offline/session streaming bị giới hạn để giảm abuse download.
- Có cleanup tránh memory map tăng vô hạn.
- Hỗ trợ Cloudflare IP thật qua `CF-Connecting-IP`.

Giới hạn:

- Rate limiter hiện là in-memory. Nếu scale nhiều instance, mỗi instance có quota riêng.
- Chưa có blocklist tạm thời sau nhiều lần login fail ở tầng account.
- Chưa có risk score theo user agent, ASN hoặc quốc gia.

Khuyến nghị khi deploy nhiều instance:

- Chuyển rate limiter sang Redis/Bucket4j/Resilience4j distributed rate limit.
- Thêm lockout mềm theo account cho login fail liên tiếp.
- Thêm dashboard theo dõi `RATE_LIMIT_EXCEEDED`.

## CORS

CORS cấu hình tại `SecurityConfig`:

```java
configuration.setAllowedOrigins(allowedOrigins);
configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
configuration.setAllowedHeaders(List.of("*"));
configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
configuration.setAllowCredentials(true);
configuration.setMaxAge(3600L);
```

Allowed origins lấy từ:

```properties
app.cors.allowed-origins=${APP_CORS_ALLOWED_ORIGINS:http://localhost:3000}
```

Điểm mạnh:

- Không hardcode wildcard `*` cho backend credential API.
- Cho phép credentials để cookie auth hoạt động.
- Có max age cho preflight cache.

Khuyến nghị production:

```properties
APP_CORS_ALLOWED_ORIGINS=https://giophim.libsys.me
```

Không dùng `*` khi `allowCredentials=true`.

## Secure video streaming

Tài liệu chi tiết nằm ở:

- `docs/SECURE_VIDEO_STREAMING_FLOW.md`

Backend dùng HLS AES-128 soft-DRM:

- MP4 gốc upload bởi admin.
- Backend/FFmpeg chuyển sang playlist `.m3u8` và segment `.ts`.
- Segment được mã hóa AES-128.
- AES key lưu ngoài thư mục public.
- Playlist chứa URI key endpoint backend.
- Browser/HLS.js lấy key từ backend sau khi backend kiểm tra quyền.

Storage liên quan:

```properties
app.storage.media.hls-directory=${MEDIA_HLS_DIRECTORY:F:/movie-storage/hls}
app.storage.media.keys-directory=${MEDIA_KEYS_DIRECTORY:F:/movie-storage/keys}
```

Quy tắc bảo mật:

- `hls-directory` có thể public qua Nginx để serve playlist/segment.
- `keys-directory` không được public qua Nginx.
- Key response phải `Cache-Control: no-store`.
- Frontend không hardcode key.
- Backend luôn quyết định user có quyền lấy key không.

Giới hạn:

- Đây là soft-DRM, không phải Widevine/FairPlay/PlayReady.
- User đã có quyền xem vẫn có thể trích xuất key từ runtime bằng devtools nâng cao.
- Không chống được quay màn hình.

## Stream key access control

Các controller liên quan:

- `StreamKeyController`
- `StreamSessionController`
- `OfflinePackageController`
- `OfflineKeyController`

Quyền truy cập hiện tại:

- Stream session yêu cầu authenticated.
- Offline package yêu cầu authenticated.
- HLS key endpoint public ở SecurityConfig nhưng controller/use case tự kiểm tra quyền.
- Offline key endpoint public ở SecurityConfig nhưng yêu cầu offline token hợp lệ.

Lý do để key endpoint public ở filter chain:

- Safari/iOS native HLS có hạn chế gửi `Authorization` header trong request key.
- Backend xử lý quyền bằng session/query token/offline token thay vì chỉ dựa vào header.
- Cách này giúp mobile playback hoạt động nhưng vẫn giữ kiểm soát ở application layer.

## Offline download security

File chính:

- `GetOfflinePackageUseCase.java`
- `GetOfflineHlsKeyUseCase.java`
- `OfflineTokenService.java`

Flow offline:

1. User đã đăng nhập gọi offline package endpoint.
2. Backend kiểm tra episode và movie tồn tại.
3. Backend kiểm tra quyền chất lượng bằng `SubscriptionAccessService`.
4. Backend tạo offline token ký bằng JWT secret.
5. Token chứa `userId`, `episodeId`, `quality`, `type=offline`.
6. Token hết hạn sau 48 giờ.
7. Khi lấy offline key, backend verify token, episodeId và quality.
8. Nếu hợp lệ, backend trả key bytes từ private key storage.

Thông số:

| Thành phần | Giá trị |
|---|---:|
| Offline token TTL | 48 giờ |
| Signing key | `jwt.secret-key` |
| Token claims | subject userId, episodeId, quality, type |

Điểm mạnh:

- File HLS segment offline vẫn là encrypted segment.
- Key chỉ lấy được nếu offline token hợp lệ.
- Offline token bị giới hạn theo episode và quality.
- Token có expiry server-verified.

Giới hạn:

- Nếu user đã lấy được key trong thời hạn token, client có thể cache để phát offline.
- Đây vẫn không phải DRM hệ điều hành.
- Chưa có revoke offline token theo device/session vì token stateless.

Khuyến nghị nếu muốn siết hơn:

- Thêm `deviceId` claim vào offline token.
- Lưu offline grant vào database để có thể revoke.
- Ràng buộc token với user agent/device session.
- Giảm TTL theo gói subscription hoặc policy admin.

## Upload media security

Các upload endpoint admin được bảo vệ bởi role admin.

Luồng upload media theo tài liệu streaming:

- Admin upload MP4 bằng multipart.
- Backend kiểm tra quyền admin.
- Backend kiểm tra file không rỗng.
- Backend kiểm tra dung lượng tối đa.
- Backend kiểm tra MIME type hợp lệ.
- Backend kiểm tra đuôi `.mp4`.
- Backend kiểm tra MP4 signature/header.
- Backend lưu source MP4 vào data directory.
- Backend cập nhật `video_url` sang HLS URL.

Cấu hình upload:

```properties
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=${MULTIPART_MAX_FILE_SIZE:100GB}
spring.servlet.multipart.max-request-size=${MULTIPART_MAX_REQUEST_SIZE:100GB}
app.storage.media.max-upload-bytes=${MEDIA_MAX_UPLOAD_BYTES:5368709120}
```

Lưu ý:

- Multipart limit đang rất lớn để hỗ trợ upload video lớn.
- Application-level `MEDIA_MAX_UPLOAD_BYTES` mặc định 5GB là giới hạn thực tế cần enforce.
- Khi qua Cloudflare Tunnel hoặc reverse proxy, cần cấu hình upload/chunked upload phù hợp để tránh giới hạn proxy.

Khuyến nghị:

- Luôn dùng chunked upload cho file lớn.
- Không expose raw data directory ra public.
- Scan file hoặc ít nhất verify container bằng FFmpeg/ffprobe trước khi publish.
- Giới hạn concurrent transcoding job để tránh cạn CPU/RAM.

## Payment và PayOS security

File liên quan:

- `PaymentController.java`
- `PaymentService.java`
- `PayOSConfig.java`
- `PayOSBeanConfig.java`
- `PaymentReconciliationScheduler.java`

Cấu hình secret:

```properties
payos.client-id=${PAYOS_CLIENT_ID:}
payos.api-key=${PAYOS_API_KEY:}
payos.checksum-key=${PAYOS_CHECKSUM_KEY:}
```

Webhook PayOS:

- Payment webhook endpoint public để PayOS gọi vào.
- Backend dùng `payOS.webhooks().verify(webhook)` để xác minh webhook.
- Không tin trạng thái payment chỉ từ client redirect.
- Có reconciliation scheduler kiểm tra lại payment pending với PayOS.

Điểm mạnh:

- Secret PayOS lấy từ environment.
- Webhook có verify bằng SDK.
- Reconciliation giảm rủi ro webhook fail/mất request.
- Payment status mapping có terminal failed states như `CANCELLED`, `EXPIRED`, `FAILED`.

Khuyến nghị:

- Không log API key/checksum key.
- Chỉ trả thông tin payment cần thiết cho frontend.
- Giữ idempotency khi xử lý webhook để tránh cộng subscription nhiều lần.
- Theo dõi webhook failure rate.

## AI chatbot security

File liên quan:

- `ChatController.java`
- `ChatService.java`
- `OllamaClient.java`
- `ChatRequest.java`
- `ChatbotProperties.java`

Các hardening hiện có:

- `/api/v1/chat/**` yêu cầu authenticated.
- `/api/v1/chat/health` yêu cầu `ROLE_ADMIN`.
- Health response không lộ model name.
- Chat stream có rate limit `20 / giờ / user`.
- Request message giới hạn tối đa 800 ký tự.
- History giới hạn tối đa 20 item.
- Mỗi history content giới hạn tối đa 800 ký tự.
- Ollama client có timeout hữu hạn.
- Output model giới hạn `num_predict=256`.
- Thread pool stream bị giới hạn, không dùng cached thread pool vô hạn.

Thread pool hiện tại:

| Tham số | Giá trị |
|---|---:|
| Core pool size | 2 |
| Max pool size | 8 |
| Queue size | 32 |
| Keep alive | 30 giây |
| Rejection policy | AbortPolicy |

Timeout hiện tại:

| Loại | Giá trị |
|---|---:|
| Connect timeout | 5 giây |
| Write timeout | 15 giây |
| Read timeout default | 60 giây |
| Call timeout | read timeout + 10 giây |

Điểm mạnh:

- Giảm rủi ro prompt spam làm treo server.
- Giảm rủi ro DoS qua SSE stream dài.
- Giảm thông tin lộ qua health endpoint.
- Chat cần đăng nhập, tránh public abuse model.

Khuyến nghị:

- Thêm moderation hoặc rule-based block cho prompt nguy hiểm nếu cần.
- Thêm audit log cho abuse nhiều lần.
- Thêm global concurrency limiter nếu Ollama chạy cùng máy backend.
- Thêm fallback friendly error khi thread pool queue đầy.

## Support contact anti-spam

Support contact public nhưng có hai lớp bảo vệ:

- Rate limit interceptor: `3 / 10 phút / IP`.
- Service-level Redis rate limit trong `ContactMessageService`.

Lợi ích:

- Form public vẫn dùng được.
- Spam email/support được giảm ở cả application và service layer.
- Redis giúp rate limit support ổn định hơn nếu service restart.

## Web push security

Cấu hình VAPID:

```properties
vapid.public-key=${VAPID_PUBLIC_KEY}
vapid.private-key=${VAPID_PRIVATE_KEY}
vapid.subject=${VAPID_SUBJECT:mailto:noreply@giophim.libsys.me}
```

Các endpoint push yêu cầu authenticated:

```text
/api/v1/push/**
```

Điểm bảo mật:

- VAPID private key lấy từ environment.
- User phải đăng nhập mới đăng ký/quản lý push subscription.
- Không public endpoint mutate push subscription.

Khuyến nghị:

- Không log endpoint push subscription đầy đủ nếu không cần.
- Cho phép user revoke subscription theo device.
- Cleanup subscription lỗi khi push provider trả gone/invalid.

## Device session security

Endpoint:

```text
/api/v1/device-sessions/**
```

yêu cầu authenticated.

Mục đích:

- Theo dõi hoặc quản lý thiết bị đăng nhập.
- Hỗ trợ giới hạn session/thiết bị nếu service áp dụng.
- Là nền tảng để siết streaming/offline theo thiết bị.

Khuyến nghị:

- Ràng buộc stream session và offline token với device session.
- Cho user revoke device không nhận ra.
- Ghi nhận last seen, IP và user agent đã rút gọn.

## Database và persistence security

Cấu hình:

```properties
spring.jpa.open-in-view=false
spring.jpa.show-sql=${JPA_SHOW_SQL:false}
logging.level.org.hibernate.SQL=OFF
logging.level.org.hibernate.orm.jdbc.bind=OFF
```

Điểm mạnh:

- Tắt SQL log và bind parameter log mặc định để tránh lộ dữ liệu nhạy cảm.
- `open-in-view=false` giảm truy cập lazy ngoài transaction.
- Repository/JPA dùng parameter binding mặc định, giảm SQL injection so với query nối chuỗi.

Khuyến nghị:

- Không bật `JPA_SHOW_SQL=true` ở production.
- Không log request body chứa password/token/OTP.
- Dùng least privilege cho database user production.

## Secrets và cấu hình môi trường

Các secret chính lấy từ environment:

| Secret | Env |
|---|---|
| JWT signing key | `JWT_SECRET_KEY` |
| Database password | `DATABASE_PASSWORD` |
| Mail password | `MAIL_PASSWORD` |
| PayOS API key | `PAYOS_API_KEY` |
| PayOS checksum key | `PAYOS_CHECKSUM_KEY` |
| OAuth Google secret | `OAUTH_GOOGLE_CLIENT_SECRET` |
| Redis password | `REDIS_PASSWORD` |
| VAPID private key | `VAPID_PRIVATE_KEY` |

Nguyên tắc:

- Không commit secret production vào Git.
- Không log secret.
- Secret local chỉ dùng phát triển.
- Rotate secret nếu nghi ngờ lộ.

## Logging và error handling

Hệ thống có HTTP logging filter và global exception handling theo `ErrorCode`.

Điểm tích cực:

- Có chuẩn error code thống nhất.
- Rate limit trả lỗi riêng `RATE_LIMIT_EXCEEDED`.
- SQL và bind logs tắt mặc định.
- Security log level cấu hình bằng environment.

Khuyến nghị:

- Mask các header nhạy cảm: `Authorization`, `Cookie`, `Set-Cookie`.
- Không log OTP, password, refresh token, access token.
- Tránh trả stack trace ra response production.
- Theo dõi 401, 403, 429 theo IP/user để phát hiện attack.

## Actuator và system status

Cấu hình actuator:

```properties
management.endpoints.web.exposure.include=${MANAGEMENT_ENDPOINTS_WEB_INCLUDE:health,info,metrics}
management.endpoint.health.show-details=when-authorized
```

Endpoint public:

- `/actuator/health`
- `/api/v1/system/status`

Điểm mạnh:

- Health endpoint public giúp uptime monitor.
- Health details chỉ hiện khi authorized.
- System status kiểm tra các component như database, media storage, PayOS config.

Khuyến nghị:

- Không expose actuator `env`, `beans`, `heapdump`, `threaddump` public.
- Nếu public status lộ quá nhiều thông tin vận hành, giảm detail ở production.

## Cloudflare và reverse proxy

Backend rate limiter đã ưu tiên `CF-Connecting-IP`.

Điểm cần cấu hình ngoài backend:

- Reverse proxy phải forward `CF-Connecting-IP` hoặc `X-Forwarded-For` đúng.
- Chỉ tin các header này nếu request đi qua proxy tin cậy.
- Cloudflare Tunnel có thể giới hạn upload/request size tùy gói và route.
- Video lớn nên dùng chunked upload hoặc direct-to-storage nếu production.

Khuyến nghị:

- Bật HTTPS end-to-end.
- Bật WAF rules cho login, register, reset password.
- Bật bot fight/challenge cho traffic bất thường nếu cần.
- Không cho client đi thẳng vào backend bypass Cloudflare nếu đang dựa vào Cloudflare protection.

## OWASP Top 10 coverage

| OWASP nhóm rủi ro | Cơ chế hiện có |
|---|---|
| Broken Access Control | Spring Security matcher, `@PreAuthorize`, authenticated default |
| Cryptographic Failures | BCrypt, JWT HMAC, SecureRandom refresh token, HLS AES-128 |
| Injection | JPA repositories, validation DTO, không thấy pattern nối SQL trực tiếp trong các flow chính |
| Insecure Design | Role-based admin, stream key gate, offline TTL, payment verify |
| Security Misconfiguration | CORS env-based, health detail restricted, SQL logs off |
| Vulnerable Components | Maven dependency quản lý qua `pom.xml`; cần audit định kỳ |
| Identification/Auth Failures | JWT, refresh token revoke, OTP, trusted device, rate limit login |
| Software/Data Integrity Failures | PayOS webhook verify, signed offline token |
| Logging/Monitoring Failures | HTTP logging, status endpoint, actuator health; cần dashboard thêm |
| SSRF | Không thấy user-controlled outbound URL trong flow chính; PayOS/Ollama URL từ config |

## Các điểm còn yếu hoặc cần cân nhắc

### In-memory rate limiter

Rủi ro:

- Khi scale nhiều backend instance, mỗi instance giữ quota riêng.
- Restart app reset toàn bộ counters.

Khuyến nghị:

- Dùng Redis distributed rate limit.
- Hoặc Bucket4j với Redis/Hazelcast backend.

### Access token TTL còn dài

Rủi ro:

- Mặc định 24 giờ làm access token bị lộ sống lâu hơn.

Khuyến nghị:

- Production nên dùng 15 đến 60 phút.
- Refresh token vẫn giữ 30 ngày nhưng có revoke.

### Offline token chưa revoke được

Rủi ro:

- Offline token ký stateless, còn hiệu lực đến khi hết hạn.

Khuyến nghị:

- Lưu offline grant DB nếu cần revoke theo thiết bị.
- Thêm `deviceId` và `jti` claim.

### HLS AES-128 không phải DRM

Rủi ro:

- User hợp lệ có thể trích xuất key.
- Không chống quay màn hình.

Khuyến nghị:

- Đồ án có thể dùng soft-DRM hiện tại.
- Production thương mại cần Widevine/FairPlay/PlayReady.

### Webhook endpoint public

Rủi ro:

- Public endpoint có thể bị spam request giả.

Cơ chế hiện tại:

- PayOS SDK verify webhook.

Khuyến nghị:

- Thêm rate limit riêng cho webhook.
- Log verify fail ở mức warn có kiểm soát.
- Idempotency tuyệt đối khi update subscription.

### Swagger public

Rủi ro:

- Public docs giúp attacker enumerate endpoint.

Khuyến nghị:

- Cho phép trong dev.
- Production nên bảo vệ bằng admin/basic auth hoặc tắt.

## Checklist production security

### Environment

- [ ] `JWT_SECRET_KEY` đủ dài, random, không dùng secret local.
- [ ] `APP_COOKIE_SECURE=true`.
- [ ] `APP_COOKIE_SAME_SITE` phù hợp deployment.
- [ ] `APP_CORS_ALLOWED_ORIGINS` chỉ chứa domain frontend production.
- [ ] `DATABASE_PASSWORD` đặt bằng secret manager hoặc env an toàn.
- [ ] `PAYOS_API_KEY` và `PAYOS_CHECKSUM_KEY` đặt bằng env.
- [ ] `VAPID_PRIVATE_KEY` đặt bằng env.
- [ ] `OAUTH_GOOGLE_CLIENT_SECRET` đặt bằng env.

### Backend

- [ ] Không bật SQL bind log production.
- [ ] Không expose actuator nhạy cảm.
- [ ] Swagger tắt hoặc bảo vệ ở production.
- [ ] Rate limit hoạt động với IP thật sau Cloudflare.
- [ ] Global exception không trả stack trace.
- [ ] Request logging mask `Authorization` và `Cookie`.

### Streaming

- [ ] `MEDIA_KEYS_DIRECTORY` không nằm trong Nginx public root.
- [ ] HLS key response không cache.
- [ ] Segment public chỉ là encrypted segment.
- [ ] Raw MP4 không public.
- [ ] Offline token TTL đúng policy.
- [ ] Stream session endpoint yêu cầu authenticated.

### Payment

- [ ] PayOS webhook verify bắt buộc.
- [ ] Webhook handler idempotent.
- [ ] Pending payment reconciliation bật.
- [ ] Không tin client redirect để kích hoạt subscription nếu chưa verify provider.

### AI chatbot

- [ ] Chat stream yêu cầu authenticated.
- [ ] Chat health admin-only.
- [ ] Timeout Ollama hữu hạn.
- [ ] Thread pool bounded.
- [ ] Input và history có validation.
- [ ] Rate limit chat hoạt động.

## Kết luận

Backend hiện có nền tảng bảo mật khá đầy đủ cho đồ án movie streaming:

- Có xác thực JWT và refresh token revoke.
- Có cookie `HttpOnly`.
- Có phân quyền admin/user rõ ràng.
- Có rate limit theo nhóm endpoint nhạy cảm.
- Có HLS AES-128 soft-DRM cho streaming.
- Có offline token TTL 48 giờ.
- Có PayOS webhook verification và reconciliation.
- Có hardening cho AI chatbot.
- Có secret cấu hình qua environment.

Các giới hạn chính không phải lỗi logic, mà là mức bảo vệ theo phạm vi đồ án:

- HLS AES-128 không thay thế DRM thật.
- Rate limiter in-memory chưa phù hợp scale nhiều instance.
- Offline token stateless chưa revoke theo device được.
- Swagger public và access token TTL dài cần cân nhắc khi production.

Nếu triển khai production thật, ưu tiên cải tiến theo thứ tự:

1. Redis distributed rate limit.
2. Giảm access token TTL.
3. Bảo vệ hoặc tắt Swagger public.
4. Thêm revoke/offline grant theo device.
5. Thêm monitoring cho 401, 403, 429, webhook fail, chat overload.
6. Cấu hình WAF/Cloudflare cho auth và webhook endpoints.
