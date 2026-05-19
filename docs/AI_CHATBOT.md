# Gió Phim Bot - Tài liệu hệ thống AI

Tài liệu này mô tả toàn bộ tính năng chatbot AI offline được tích hợp vào nền tảng Gió Phim. Bao gồm kiến trúc, công nghệ, flow xử lý, nguồn dữ liệu, bảo mật, hiệu năng và cách vận hành.

## Mục lục

1. [Tổng quan](#1-tong-quan)
2. [Mục tiêu và phạm vi](#2-muc-tieu-va-pham-vi)
3. [Công nghệ sử dụng](#3-cong-nghe-su-dung)
4. [Kiến trúc tổng thể](#4-kien-truc-tong-the)
5. [Cấu trúc module](#5-cau-truc-module)
6. [Flow xử lý end-to-end](#6-flow-xu-ly-end-to-end)
7. [System prompt và quy tắc hành vi](#7-system-prompt-va-quy-tac-hanh-vi)
8. [Cơ chế cá nhân hóa](#8-co-che-ca-nhan-hoa)
9. [Bot biết những gì và không biết những gì](#9-bot-biet-nhung-gi-va-khong-biet-nhung-gi)
10. [API endpoints](#10-api-endpoints)
11. [Giao thức streaming SSE](#11-giao-thuc-streaming-sse)
12. [Frontend UI và UX](#12-frontend-ui-va-ux)
13. [Lưu trữ lịch sử](#13-luu-tru-lich-su)
14. [Cấu hình](#14-cau-hinh)
15. [Bảo mật](#15-bao-mat)
16. [Hiệu năng và độ trễ](#16-hieu-nang-va-do-tre)
17. [Khởi động và vận hành](#17-khoi-dong-va-van-hanh)
18. [Hạn chế hiện tại](#18-han-che-hien-tai)
19. [Hướng phát triển tương lai](#19-huong-phat-trien-tuong-lai)
20. [Khắc phục sự cố](#20-khac-phuc-su-co)

---

## 1. Tổng quan

Gió Phim Bot là trợ lý AI hội thoại được nhúng trực tiếp vào website, hoạt động hoàn toàn offline trên máy chủ phát triển nhờ Ollama. Người dùng có thể trò chuyện với bot để được gợi ý phim, hướng dẫn dùng nền tảng, hoặc hỏi đáp về tính năng. Bot phản hồi theo thời gian thực, từng token một, qua giao thức Server-Sent Events.

Mục đích của bot:

- Tăng tương tác và độ gắn bó của người dùng với nền tảng.
- Hỗ trợ người dùng mới làm quen với các tính năng (đăng ký Premium, tải phim offline, quản lý lịch sử xem).
- Cá nhân hóa trải nghiệm bằng cách dựa vào lịch sử xem và gợi ý đã có sẵn của hệ thống.
- Không phụ thuộc nhà cung cấp AI bên ngoài, không phát sinh chi phí API, không gửi dữ liệu người dùng ra ngoài.

## 2. Mục tiêu và phạm vi

Bot được thiết kế để phục vụ các nghiệp vụ sau:

- Gợi ý phim theo thể loại, năm phát hành, tâm trạng người dùng.
- Trả lời câu hỏi về cách dùng tính năng có sẵn trên website.
- Định hướng người dùng đến đúng trang con khi cần (ví dụ /pricing, /watchlist, /downloads).
- Tâm sự nhẹ về phim, đưa ý kiến chủ quan có kiểm soát.

Bot không được thiết kế để:

- Cung cấp thông tin cá nhân nhạy cảm như email, mật khẩu, số thẻ thanh toán.
- Thay thế quản trị viên hoặc nhân viên hỗ trợ thực tế.
- Truy cập trực tiếp vào cơ sở dữ liệu để truy vấn tự do.
- Xác thực thanh toán hoặc thực hiện thao tác ghi vào hệ thống.

## 3. Công nghệ sử dụng

### Backend

- Java 21 với Spring Boot 4.0.5 (theo cấu hình hiện tại của dự án).
- Spring Web MVC để cung cấp endpoint REST và Server-Sent Events thông qua SseEmitter.
- OkHttp 4.11 cho việc gọi Ollama HTTP API và đọc stream NDJSON.
- Jackson cho serialization JSON.
- Bean validation chuẩn jakarta.
- Cấu trúc Domain Driven Design quen thuộc: domain, application, infrastructure, presentation.

### AI Runtime

- Ollama là runtime AI cục bộ, chạy ở cổng 11434.
- Mô hình mặc định: qcwind/qwen3-8b-instruct-Q4-K-M:latest. Đây là biến thể quantize 4-bit của Qwen3 8B, dung lượng khoảng 5.2 GB, đủ nhẹ để chạy trên máy có 16 GB RAM trở lên. Đã hỗ trợ tiếng Việt khá tốt.
- Có thể đổi sang mô hình khác bằng cách thay biến môi trường ollama.model trong application-local.properties.

### Frontend

- Next.js 15 App Router với TypeScript.
- Material UI 7 cho hệ thống component.
- React Hooks tự viết để quản lý state hội thoại và streaming.
- Fetch API gốc cùng ReadableStreamDefaultReader để nhận SSE.

## 4. Kiến trúc tổng thể

Bot là một thành phần độc lập trong backend, không can thiệp vào các module nghiệp vụ khác. Nó chỉ đọc dữ liệu thông qua các UseCase đã có sẵn (gợi ý phim, lịch sử xem, phim trending) để tạo ngữ cảnh cho mô hình.

```
+------------------+        +-----------------------+        +------------------+
|  Frontend (Next) |        |  Spring Boot Backend  |        |  Ollama Runtime  |
|                  |        |                       |        |                  |
| GioPhimBot FAB   | -----> | ChatController        | -----> | /api/chat (NDJSON)
| ChatPanel        | <----- | (SSE)                 | <----- |  Qwen3 8B model  |
| useChatStream    |        | ChatService           |        +------------------+
| chatbot-service  |        | OllamaClient          |
+------------------+        | ChatContextBuilder    |
                            | PromptTemplates       |
                            | ChatUserContext       |
                            +-----------+-----------+
                                        |
                                        v
                            +-----------------------+
                            |  Use cases sẵn có     |
                            |  - Trending movies    |
                            |  - Continue watching  |
                            |  - Recommendations    |
                            +-----------------------+
```

Luồng dữ liệu chính:

- Frontend gửi câu hỏi qua HTTPS POST đến /api/v1/chat/stream cùng tối đa 12 message lịch sử gần nhất.
- Backend xác định người dùng có đăng nhập không, build prompt hệ thống, ghép ngữ cảnh động, sau đó gọi Ollama bằng OkHttp.
- Ollama trả về NDJSON (mỗi dòng là một JSON delta token). Backend đọc dòng nào, đẩy ra SSE dòng đó.
- Frontend đọc SSE bằng ReadableStream, gắn từng token vào bubble đang mở.

## 5. Cấu trúc module

Toàn bộ tính năng nằm trong package com.hoaug.movieapi.modules.chatbot, theo lớp:

### Domain

- ChatRole: enum gồm SYSTEM, USER, ASSISTANT, TOOL.
- ChatMessage: record chứa role, nội dung, thời điểm tạo. Có factory method system, user, assistant, tool để code gọn.

### Application

- ChatbotProperties: bind các giá trị từ properties (ollama.base-url, ollama.model, ollama.timeout-seconds, chatbot.max-history-messages, chatbot.rate-limit-per-hour).
- PromptTemplates: cung cấp baseSystemPrompt, guestContextHint, userContextHint. Đây là nơi viết prompt tiếng Việt và quy tắc hành vi.
- OllamaClient: lớp gói gọn việc gọi Ollama. Method streamChat đọc NDJSON dòng nào parse dòng đó, gọi callback onDelta cho mỗi token. Method isHealthy gọi /api/tags để kiểm tra Ollama có sẵn sàng không.
- ChatService: orchestration chính. Build danh sách ChatMessage từ system prompt, lịch sử, câu mới. Sau đó gọi OllamaClient.
- ChatContextBuilder: tổng hợp dữ liệu trang động (trending, lịch sử, gợi ý) thành đoạn text gắn vào system prompt.
- ChatUserContext: record chứa thông tin user đã sanitize (id, displayName, tier).
- ChatUserContextProvider: dịch Authentication thành ChatUserContext, đọc User từ AuthUserRepository và quyết định tier dựa trên premiumExpiryDate.
- ChatbotException: ngoại lệ riêng để runtime báo lỗi cho controller.

### Presentation

- ChatRequest: DTO request. Có message bắt buộc (tối đa 2000 ký tự) và history là List<ChatHistoryItem>.
- ChatChunk: DTO event SSE đẩy về frontend, có 4 type: delta, done, error, tool.
- ChatController: expose 2 endpoint health check và stream. Stream dùng SseEmitter và một Executors.newCachedThreadPool riêng tên chatbot-stream.

## 6. Flow xử lý end-to-end

Mỗi lần người dùng gửi tin nhắn, hệ thống đi qua 9 bước:

1. Người dùng gõ nội dung trong ChatComposer của ChatPanel rồi nhấn Enter hoặc nút Send.
2. Hook useChatStream tạo userMessage và assistantPlaceholder pending=true vào state, đồng thời lấy 12 message gần nhất làm history.
3. chatbot-service gọi fetch POST tới /api/v1/chat/stream với credentials include và header Authorization Bearer nếu có accessToken trong localStorage.
4. ChatController nhận request, validate message qua jakarta, lấy Authentication từ Spring Security, dùng ChatUserContextProvider tạo ChatUserContext.
5. ChatController submit job vào executor pool, mở SseEmitter timeout dài (timeout của Ollama cộng buffer 10s).
6. ChatService build danh sách ChatMessage. System prompt = baseSystemPrompt + (guestContextHint hoặc userContextHint) + contextBlock. ContextBlock được lấy từ ChatContextBuilder.
7. OllamaClient gọi POST http://localhost:11434/api/chat với payload gồm model, danh sách messages, stream true, options (temperature 0.6, num_predict 512, top_p 0.9). OkHttp đọc body theo dòng, mỗi dòng là một JSON gồm message.content là token mới và done là cờ kết thúc.
8. Mỗi token nhận được, ChatService gọi callback đẩy lên SseEmitter dưới dạng event JSON ChatChunk type=delta. Frontend đọc dòng SSE, parse JSON, append vào assistantMessage.content.
9. Khi Ollama trả done=true, OllamaClient kết thúc loop, ChatService đẩy event done. ChatController gọi emitter.complete. Frontend đặt pending=false cho tin nhắn assistant.

Nếu xảy ra lỗi tại bất cứ bước nào ở backend, controller bắt ChatbotException hoặc Exception generic và đẩy event type=error với message thân thiện, sau đó complete emitter. Frontend hiển thị bubble màu cảnh báo.

## 7. System prompt và quy tắc hành vi

System prompt là tin nhắn đầu tiên trong cuộc hội thoại, định hình cách bot phản hồi. Prompt được viết tiếng Việt, súc tích, có 5 phần:

### 7.1. Định danh

Bot tự nhận là Gió Phim Bot, trợ lý AI thân thiện của nền tảng xem phim Gió Phim. Khi được hỏi về danh tính, bot phải nói rõ là AI, không giả vờ là người thật.

### 7.2. Quy tắc bắt buộc

- Trả lời tiếng Việt, ngắn gọn, dễ hiểu, có thể dùng emoji nhẹ.
- Văn phong gần gũi, không trang trọng quá mức.
- Không tiết lộ email, mật khẩu, thông tin thanh toán, JWT, dữ liệu admin.
- Không bịa thông tin. Khi không chắc, gợi ý người dùng vào trang phù hợp.
- Ưu tiên trả lời dưới 4 câu, mỗi câu ngắn.

### 7.3. Bản đồ trang

Prompt liệt kê các route chính: /movies, /tv, /discovery, /favorites, /history, /watchlist, /downloads, /pricing, /profile, /support/faq. Nhờ vậy bot có thể trả lời định hướng đúng đường dẫn khi được hỏi.

### 7.4. Phạm vi nghiệp vụ

Bot được phép gợi ý phim, giải thích cách dùng tính năng, tâm sự nhẹ về phim. Các chủ đề ngoài phạm vi này, bot sẽ từ chối hoặc dẫn về /support/faq.

### 7.5. Hint ngữ cảnh

Tùy theo người dùng đăng nhập hay không, prompt nối thêm:

- Khi guest: nhắc người dùng có thể đăng nhập tại /auth/login để được gợi ý cá nhân hóa.
- Khi authenticated: ghi rõ tên hiển thị và tier (Premium hoặc Miễn phí) để bot xưng hô đúng.

## 8. Cơ chế cá nhân hóa

Bot không gọi tool động cũng không truy vấn SQL trực tiếp. Thay vào đó, mỗi request mới sẽ gắn thêm một context block vào system prompt. Đây là cách rẻ, đơn giản, ổn định, ít token, ít latency.

ChatContextBuilder trả về một đoạn text dạng:

```
Dữ liệu trang (cập nhật mỗi lần hỏi):
- The Last of Us (2024) (phim bộ) — /movies/the-last-of-us
- Dune Part Two (2024) (phim lẻ) — /movies/dune-part-two
... (tối đa 6 phim trending)

Lịch sử xem gần đây của người dùng:
- Inception (tập 1) — đang xem dở, /watch/inception
... (tối đa 4 phim, chỉ khi user đã đăng nhập)

Gợi ý cá nhân hoá hiện có:
- The Matrix — /movies/the-matrix
... (tối đa 4 phim, chỉ khi user đã đăng nhập)

Khi gợi ý phim, hãy ưu tiên dùng các phim trên. Nếu user hỏi phim không có trong danh sách, hãy gợi ý họ vào /discovery hoặc /movies để khám phá thêm.
```

Tổng cộng tối đa 14 phim được nhúng vào prompt, cộng thêm dòng hướng dẫn ưu tiên. Token cost ước tính dưới 600 token, không gây áp lực cho mô hình.

### 8.1. Nguồn dữ liệu cho context

| Nguồn | UseCase được dùng | Khi nào hoạt động |
|---|---|---|
| Top 6 trending tuần | GetTrendingMoviesUseCase | Mọi user (kể cả guest) |
| 4 phim đang xem dở | GetContinueWatchingUseCase | User đã đăng nhập |
| 4 gợi ý cá nhân | GetMyRecommendationsUseCase | User đã đăng nhập |

UseCase trending được cache @Cacheable("movies", key="trending:N") nên không gọi DB mỗi lần. Continue watching và recommendations cũng tận dụng các adapter sẵn có của module watchhistory và recommendation.

### 8.2. Truyền user identity an toàn

ChatUserContext chỉ giữ id, displayName, tier. Không bao giờ chứa email, mật khẩu, JWT, hay chi tiết thanh toán. Khi build context, chỉ id được dùng để truy vấn UseCase. displayName và tier được nhúng vào prompt dạng plain text. Khi user là admin, prompt vẫn xử lý y như user thường, không cấp quyền đặc biệt.

## 9. Bot biết những gì và không biết những gì

### 9.1. Bot biết

- Tên 6 phim trending trong tuần và slug để dẫn link.
- Lịch sử xem dở của user nếu đã đăng nhập, kèm tên tập đang xem.
- 4 phim hệ thống gợi ý cho user.
- Bản đồ các trang chính trong website và mục đích từng trang.
- Quy tắc nghiệp vụ về Premium, FAQ, contact, downloads.
- Khả năng tâm sự nhẹ về phim do mô hình Qwen3 đã được huấn luyện sẵn.

### 9.2. Bot không biết

- Mô tả chi tiết, dàn diễn viên, đạo diễn, năm sản xuất, đánh giá của các phim không nằm trong 14 phim được context.
- Tìm kiếm tự do theo từ khóa hoặc tên phim cụ thể không được pre-load.
- Trạng thái thanh toán, ngày hết hạn Premium chính xác. Bot chỉ biết tier hiện tại.
- Thông tin của user khác hoặc dữ liệu admin.
- Sự kiện thời gian thực ngoài hệ thống (tin tức, lịch chiếu rạp).

### 9.3. Bot xử lý câu hỏi ngoài phạm vi như thế nào

Theo prompt rule, khi không có thông tin chính xác, bot phải nói rõ và gợi ý người dùng vào /support/faq hoặc trang phù hợp. Nếu user hỏi câu hoàn toàn ngoài chủ đề (ví dụ thời tiết, code), bot sẽ trả lời chung chung rồi nhắc lại vai trò trợ lý xem phim.

## 10. API endpoints

Tất cả endpoint nằm dưới prefix /api/v1/chat và đều permitAll trong SecurityConfig để guest cũng có thể chat. JWT filter vẫn populate Authentication nếu user đăng nhập, nên cá nhân hóa hoạt động bình thường.

### 10.1. GET /api/v1/chat/health

Trả về thông tin Ollama có sẵn sàng không và model đang dùng.

```
GET /api/v1/chat/health

200 OK
{
  "ollamaReady": true,
  "model": "qcwind/qwen3-8b-instruct-Q4-K-M:latest"
}
```

Endpoint này không cần auth, hữu ích cho frontend hiển thị trạng thái hoặc tắt FAB khi backend không thấy Ollama.

### 10.2. POST /api/v1/chat/stream

Endpoint chính, trả về Server-Sent Events.

Request body:

```
{
  "message": "Gợi ý cho tôi 1 phim hành động hay",
  "history": [
    { "role": "user", "content": "Xin chào" },
    { "role": "assistant", "content": "Chào bạn, mình giúp gì được?" }
  ]
}
```

Validation:

- message: bắt buộc, không trống, tối đa 2000 ký tự.
- history: tùy chọn. Mỗi item có role (user, assistant, system, tool, không phân biệt hoa thường) và content. Backend chỉ lấy max 10 message cuối, bỏ qua role system.

Response: chuỗi SSE liên tiếp tới khi emitter complete. Mỗi event có dạng:

```
event: message
data: {"type":"delta","content":"Xin "}
```

## 11. Giao thức streaming SSE

### 11.1. Định dạng event

Backend dùng SseEmitter và build event qua SseEmitter.event().name("message").data(json, MediaType.APPLICATION_JSON). Tất cả message stream cùng tên message để frontend không phải đăng ký nhiều listener.

ChatChunk có 4 loại type:

- delta: chứa một đoạn text (token), cần append vào bubble assistant.
- done: tín hiệu kết thúc bình thường, frontend tắt pending.
- error: có lỗi xảy ra, content là thông báo thân thiện hiển thị cho user.
- tool: chỗ dự phòng cho tương lai khi muốn báo bot vừa gọi một tool. Hiện không phát.

### 11.2. Tại sao dùng fetch streaming thay vì EventSource

EventSource native không gửi được header Authorization. Vì cần truyền JWT, frontend dùng fetch POST với header tự custom, sau đó đọc response.body bằng ReadableStreamDefaultReader. Buffer được tách bằng dấu hai dòng trống đúng chuẩn SSE. Mỗi event trích lấy dòng bắt đầu data:, parse JSON, gọi onEvent callback.

### 11.3. Hủy stream

Hook useChatStream giữ một AbortController. Khi user nhấn nút Stop hoặc đóng tab, controller.abort() được gọi. Fetch kết thúc với DOMException name AbortError. Hook đặt pending=false cho assistant message hiện tại nhưng không đánh dấu lỗi.

## 12. Frontend UI và UX

### 12.1. Thành phần

- GioPhimBot: host FAB, welcome popup, mount động ChatPanel qua next/dynamic ssr=false để tránh hydrate.
- ChatPanel: cửa sổ messenger, có header gradient, danh sách tin nhắn cuộn được, suggestion chips lúc rỗng, ChatComposer ở dưới.
- ChatBubble: bong bóng tin nhắn cho cả 2 phía, có TypingDots khi pending và BlinkCursor khi đang stream.
- ChatComposer: input pill bo tròn, tự động giãn 4 dòng, gửi bằng Enter hoặc nút Send. Khi đang stream, nút Send chuyển thành Stop.

### 12.2. Hành vi

- FAB cố định ở bottom-right, lift hover, gradient màu primary của theme (#C8102E).
- Welcome popup tự xuất hiện sau 2.8 giây vào trang lần đầu, ẩn sau 9 giây hoặc khi user đóng. Trạng thái lưu ở sessionStorage giophim:chatbot:welcomeShown để không lặp lại trong cùng tab session.
- Mở panel sẽ ngừng welcome ngay lập tức.
- Trên mobile (breakpoint sm), panel mở dạng bottom-sheet 85vh thay vì cửa sổ 380x560 desktop.
- Bot hidden tự động trên các route /admin và /auth để không gây nhiễu admin và trang đăng nhập.
- Bubble user dùng nền text-primary alpha 8%, bubble bot dùng gradient primary với box-shadow đỏ alpha 32%, đồng bộ palette dark/light.

### 12.3. UX gợi ý mở đầu

Khi messages rỗng, panel hiển thị một welcome card từ bot và 4 chip suggestion. Click vào chip sẽ gửi message ngay, giúp user mới biết phải hỏi gì. Suggestion hiện tại:

- Gợi ý cho tôi 1 phim hành động hay
- Có phim mới nào trong tháng này?
- Cách tải phim xem offline?
- Sự khác biệt giữa gói Free và Premium?

## 13. Lưu trữ lịch sử

### 13.1. Frontend

useChatStream lưu tối đa 30 message gần nhất vào localStorage key giophim:chatbot:history. Chỉ lưu các message non-pending có role user hoặc assistant. Khi reload trang, hook khởi tạo state từ key này nên cuộc trò chuyện được khôi phục.

Khi user nhấn nút Refresh trong header panel, lịch sử được xóa cả state và localStorage.

### 13.2. Backend

Backend không lưu hội thoại. Mỗi request đến đều phải mang theo history trong body. Cách này giúp tránh phải quản lý session phía server, đơn giản hóa kiến trúc, scale tốt khi triển khai nhiều instance.

Nếu cần lưu lâu dài (ví dụ phục vụ tổng hợp report sau này), có thể bổ sung Redis với TTL 24 giờ ở phase tiếp theo. Phần khung cấu hình đã có trong ChatbotProperties (rate-limit-per-hour) để chuẩn bị mở rộng.

## 14. Cấu hình

Tất cả cấu hình AI nằm trong file application-local.properties hoặc tương ứng theo profile.

```properties
ollama.base-url=http://localhost:11434
ollama.model=qcwind/qwen3-8b-instruct-Q4-K-M:latest
ollama.timeout-seconds=120

chatbot.max-history-messages=10
chatbot.rate-limit-per-hour=30
```

Ý nghĩa:

| Key | Mặc định | Ý nghĩa |
|---|---|---|
| ollama.base-url | http://localhost:11434 | Endpoint của Ollama. Có thể thay bằng host LAN nếu Ollama chạy trên máy khác. |
| ollama.model | qcwind/qwen3-8b-instruct-Q4-K-M:latest | Tên model trong Ollama. |
| ollama.timeout-seconds | 120 | Thời gian tối đa chờ một response stream hoàn tất. |
| chatbot.max-history-messages | 10 | Số message lịch sử tối đa được nạp vào prompt. |
| chatbot.rate-limit-per-hour | 30 | Đặt sẵn cho tương lai khi áp dụng rate limit Redis. Hiện chưa enforce. |

Có thể đổi mô hình bằng cách:

1. Cài model mới qua Ollama: ollama pull <model-name>.
2. Sửa ollama.model thành tên mới.
3. Restart backend.

Mô hình khuyến nghị thay thế:

- qwen2.5:3b: nhẹ, nhanh, vẫn hỗ trợ tiếng Việt nhưng kém hơn 8B.
- llama3.1:8b: chất lượng tốt, hỗ trợ tiếng Việt khá.
- gemma2:9b: yêu cầu RAM cao hơn nhưng chất lượng đầu ra ổn.

## 15. Bảo mật

### 15.1. Authentication

- Endpoint chat permitAll vì cần phục vụ cả guest. Tuy nhiên JwtAuthenticationFilter vẫn chạy và populate Authentication, nên backend phân biệt được guest và authenticated.
- ChatUserContextProvider chỉ trả ChatUserContext.guest nếu Authentication là anonymous hoặc không xác thực được.

### 15.2. Bảo vệ thông tin nhạy cảm

- System prompt cấm bot nói ra email, mật khẩu, JWT, payment, dữ liệu admin.
- ChatUserContext không chứa các trường nhạy cảm. Chỉ id, displayName, tier được phép xuất hiện.
- ChatContextBuilder không đẩy giá trị thanh toán hay session token vào prompt.
- Output bot có thể thêm bộ lọc regex post-process trong tương lai nếu phát hiện model rò rỉ thông tin.

### 15.3. Chống lạm dụng

- Validation @Size(max=2000) trên message để chặn payload lớn gây DoS.
- Backend giới hạn max 10 message lịch sử nạp prompt, ngăn user gửi history khổng lồ.
- Rate limit chưa enforce nhưng đã có placeholder. Khuyến nghị cấu hình thêm Redis token bucket khi triển khai production.
- SseEmitter có timeout cứng (timeout của Ollama cộng buffer), tự complete để giải phóng thread.

### 15.4. Network

- Ollama chỉ lắng nghe localhost. Không có cổng nào của Ollama mở ra internet.
- Backend gọi Ollama qua loopback, không có vấn đề CORS.
- Frontend gọi backend qua HTTPS giophim.libsys.me, payload và stream đều đi qua tunnel Cloudflare.

## 16. Hiệu năng và độ trễ

### 16.1. Các yếu tố ảnh hưởng

- Phần cứng máy chủ Ollama: CPU, RAM, có GPU CUDA hay không.
- Kích thước mô hình: Qwen3 8B Q4 dùng khoảng 5.5 GB RAM khi loaded.
- Độ dài context: tổng tokens system prompt cộng history cộng câu mới. Hiện trung bình 600-1500 tokens.
- Số token cần generate: option num_predict hiện đặt 512.

### 16.2. Latency dự kiến

| Tình huống | Time To First Token | Token rate |
|---|---|---|
| Lần đầu sau khi backend boot, model chưa load | 3 đến 8 giây | tùy phần cứng |
| Sau khi đã warm-up bằng dev.bat | 0.5 đến 2 giây | nhanh ngay |
| Streaming token tiếp theo | n/a | 30 đến 80 ms mỗi token trên CPU; 10 đến 30 ms mỗi token trên GPU |
| Tổng câu trả lời 150 token | 5 đến 15 giây | user thấy chữ chạy ngay từ đầu |

### 16.3. Tối ưu phía hệ thống

- dev.bat tự động warm-up bằng cách gọi /api/generate với num_predict=1 trước khi Spring Boot chạy. Sau warm-up, model đã ở trong RAM nên token đầu nhanh.
- UseCase trending được cache 5 phút theo cấu hình @Cacheable nên context block được build nhanh.
- Mỗi request stream chạy trong thread riêng từ executor pool, không khóa servlet container.

### 16.4. Tối ưu phía mô hình

- Có thể giảm num_predict trong OllamaClient từ 512 xuống 256 nếu thấy bot trả lời quá dài.
- Có thể giảm temperature từ 0.6 xuống 0.4 nếu cần phản hồi nhất quán hơn.
- Có thể bật KV cache nếu Ollama hỗ trợ trong phiên bản tương lai.

## 17. Khởi động và vận hành

### 17.1. Khởi backend với 1 lệnh

Trong thư mục movie-streaming-api có dev.bat và dev.ps1. Cả 2 đều thực hiện 4 việc:

1. Kiểm tra ollama.exe đang chạy. Nếu chưa, thực thi ollama serve trong tiến trình ngầm.
2. Đợi /api/tags phản hồi 200, max 30 giây.
3. Warm-up model bằng /api/generate prompt Hi với num_predict=1.
4. Chạy mvnw spring-boot:run với profile local.

Cách dùng:

```powershell
cd movie-backend\movie-streaming-api
.\dev.bat
```

Hoặc nếu thích PowerShell thuần:

```powershell
.\dev.ps1
```

### 17.2. Khởi frontend

```powershell
cd movie-frontend\movie-streaming-web
npm run dev
```

### 17.3. Đổi mô hình

```powershell
ollama pull qwen2.5:3b
```

Sửa ollama.model trong application-local.properties thành qwen2.5:3b. Restart backend.

### 17.4. Tắt bot tạm thời

Chỉnh GioPhimBot trong providers.tsx, comment dòng <GioPhimBot />. Hoặc đặt biến môi trường feature flag NEXT_PUBLIC_CHATBOT_ENABLED và check trong component.

## 18. Hạn chế hiện tại

- Không có tool calling. Bot không thể tra cứu phim không thuộc 14 phim được context.
- Không có rate limiting thực thi. User có thể spam request.
- Không có moderation đầu vào. Nếu user gửi nội dung phá hoại, bot chỉ dựa vào system prompt để từ chối. Không có lớp filter riêng.
- Không có audit log cho các phiên chat. Khi cần truy vết, không có dữ liệu.
- Không có evaluation tự động chất lượng output. Cần test thủ công.
- Mô hình chạy local, phụ thuộc máy phát triển. Khi deploy production cần máy chủ riêng có GPU hoặc dùng dịch vụ AI cloud.

## 19. Hướng phát triển tương lai

### 19.1. Tool calling thực thụ

Thay thế ContextBuilder bằng cơ chế gọi tool động:

- search_movies(keyword, year, genre) trả về list phim phù hợp.
- get_movie_detail(slug) trả về mô tả, cast, đạo diễn, rating.
- get_user_subscription_summary() trả về thông tin Premium tóm tắt.
- get_recommendations_for_topic(topic) trả về list phim hoạt hình, hành động, lãng mạn theo yêu cầu.

Để làm được, cần dùng mô hình hỗ trợ function calling tốt hơn (ví dụ Llama 3.1 70B hoặc OpenAI GPT-4o-mini) hoặc tự parse JSON từ đầu ra.

### 19.2. Lưu lịch sử Redis

Lưu hội thoại theo session 24 giờ giúp:

- User chat ở thiết bị này, mở thiết bị khác vẫn thấy lịch sử.
- Có dữ liệu để analytics (nội dung user hay hỏi).
- Có thể audit khi cần.

### 19.3. Rate limiting

Áp dụng token bucket Redis với 30 request mỗi giờ mỗi user (theo userId hoặc IP với guest). Nếu vượt, trả 429 Too Many Requests.

### 19.4. Multi-modal

Hỗ trợ user gửi ảnh poster hoặc screenshot, bot nhận diện và gợi ý phim tương tự. Yêu cầu mô hình vision model như LLaVA.

### 19.5. Voice

Tích hợp speech-to-text và text-to-speech để bot trở thành trợ lý giọng nói.

### 19.6. Evaluation pipeline

Tập câu hỏi mẫu chạy mỗi tuần, đo response time, đo chất lượng (LLM-as-judge), sinh report tự động.

## 20. Khắc phục sự cố

### 20.1. Bot không phản hồi, frontend chờ mãi

- Kiểm tra Ollama đang chạy: tasklist | findstr ollama. Nếu không thấy, chạy ollama serve thủ công.
- Test endpoint Ollama: curl http://localhost:11434/api/tags. Nếu không trả JSON danh sách model, restart Ollama.
- Test endpoint backend: curl http://localhost:8080/api/v1/chat/health. Phải có ollamaReady=true.
- Xem log Spring Boot có dòng Ollama returned status. Nếu có 404, model trong properties không tồn tại trong Ollama.

### 20.2. Bot nói tiếng Anh thay vì tiếng Việt

- Kiểm tra system prompt đã được build đúng. Có thể đặt log tạm thời trong ChatService.buildMessages để in danh sách messages.
- Đổi model. Một số mô hình quantize sâu có hiện tượng trôi ngôn ngữ. Thử qwen2.5:7b-instruct hoặc phi3.5:latest.
- Tăng độ rõ trong baseSystemPrompt: thêm câu Bạn LUÔN trả lời bằng tiếng Việt, không bao giờ dùng ngôn ngữ khác.

### 20.3. Bot trả lời chậm bất thường

- Kiểm tra GPU đang được dùng không: nvidia-smi nếu có CUDA. Nếu không có GPU, đây là tốc độ CPU-bound, hãy chấp nhận hoặc đổi sang model nhỏ hơn.
- Giảm num_predict.
- Đảm bảo dev.bat đã warm-up. Nếu chạy spring-boot:run thuần, lần đầu sẽ chậm.

### 20.4. SSE bị ngắt giữa chừng

- Cloudflare Tunnel có buffer mặc định 100 giây. Với câu trả lời dài, có thể cấu hình ingress idle-timeout cao hơn trong cloudflared/config.yml.
- Backend đã đặt SseEmitter timeout dài, nhưng client hoặc proxy giữa có thể đóng. Nên check Network tab trên DevTools để biết bên nào đóng trước.

### 20.5. Lỗi 401 Unauthorized

- Endpoint /api/v1/chat/** đã permitAll. Nếu vẫn 401, kiểm tra SecurityConfig đã được rebuild chưa. Restart backend bằng dev.bat.
- Hoặc do JwtAuthenticationFilter throw lỗi khi parse token. Trong trường hợp đó, xóa accessToken trong localStorage và thử lại.

### 20.6. Bot rò rỉ dữ liệu nhạy cảm

- Đây là sự cố nghiêm trọng. Bước đầu, tăng cường rule trong baseSystemPrompt: liệt kê rõ các trường cấm.
- Bước hai, thêm bộ lọc regex post-process trong ChatService streamReply, kiểm tra mỗi token gộp lại có khớp pattern email, IBAN, JWT không.
- Bước ba, kiểm tra ContextBuilder không leak dữ liệu khác (ví dụ slug nội bộ chưa publish).

---

Tài liệu này được duy trì cùng codebase. Khi sửa logic hoặc thêm tính năng cho bot, cập nhật file này tương ứng để team luôn có nguồn tham khảo chính xác.
