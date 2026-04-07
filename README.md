# Movie Streaming API - Complete Documentation

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-green)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.4-blue)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/JWT-Authentication-lightblue)](https://jwt.io/)

Comprehensive REST API for Movie Streaming Platform with User Management, Movie CRUD, Episodes, and Admin Controls.

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Setup & Installation](#setup--installation)
- [API Endpoints](#api-endpoints)
  - [Authentication APIs](#-authentication-apis-7-endpoints)
  - [Movie APIs (Public)](#-movie-apis-public-4-endpoints)
  - [Movie Admin APIs](#-movie-admin-apis-5-endpoints)
  - [User APIs](#-user-apis-8-endpoints)
- [Authentication](#authentication)
- [Error Handling](#error-handling)
- [Request/Response Examples](#requestresponse-examples)
- [Testing](#testing)

---

## Overview

A full-featured movie streaming backend API that provides:

- ✅ User authentication with JWT tokens
- ✅ Complete Movie CRUD operations
- ✅ Episode management for movies
- ✅ User profile management
- ✅ Admin controls for user and movie management
- ✅ Role-based access control (RBAC)

---

## Tech Stack

| Component      | Technology            |
| -------------- | --------------------- |
| **Language**   | Java 21               |
| **Framework**  | Spring Boot 3.3.0     |
| **Database**   | MySQL 8.0             |
| **ORM**        | JPA/Hibernate         |
| **Security**   | Spring Security + JWT |
| **Build Tool** | Maven                 |
| **API Style**  | RESTful               |

---

## Setup & Installation

### Prerequisites

- Java 21+ installed
- MySQL 8.0+ running
- Maven 3.8+
- Git

### Configuration

Set environment variables:

```bash
# Database
MYSQL_ROOT_PASSWORD=abcxyz
MYSQL_DATABASE=abcxyz
MYSQL_USER=abcxyz
MYSQL_PASSWORD=abcxyz
MYSQL_PORT=abcxyz

# API
API_PREFIX=/api/v1

# JWT
JWT_SECRET_KEY=abcxyz
JWT_ACCESS_TOKEN_EXPIRATION=86400000
```

### Run Application

```bash
# Clone project
cd movie-streaming-api

# Build
mvn clean compile

# Run
mvn spring-boot:run

# OR with debug
java -agentlib:jdwp=transport=dt_socket,server=n,suspend=y,address=localhost:5005 -jar target/movie-streaming-api-0.0.1-SNAPSHOT.jar
```

Default URL: `http://localhost:8080`

---

## API Endpoints

### Total: **24 Comprehensive Endpoints**

---

## **Authentication APIs** (7 Endpoints)

All endpoints use JSON request/response.

### 1. Register User

**Endpoint:**

```
POST /api/v1/auth/register
```

**Description:** Create new user account

**Request:**

```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "fullName": "Test User"
}
```

**Response:** (200 OK)

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "userId": 1,
  "username": "testuser",
  "email": "test@example.com",
  "message": "Đăng ký thành công"
}
```

**Error Responses:**

- `400` - Username/email already exists
- `400` - Invalid input validation

---

### 2. Login

**Endpoint:**

```
POST /api/v1/auth/login
```

**Description:** Authenticate user and get tokens

**Request:**

```json
{
  "usernameOrEmail": "testuser",
  "password": "password123"
}
```

**Response:** (200 OK)

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "userId": 1,
  "username": "testuser",
  "email": "test@example.com"
}
```

**Error Responses:**

- `401` - Invalid credentials
- `404` - User not found

---

### 3. Get Current User

**Endpoint:**

```
GET /api/v1/auth/me
```

**Description:** Get authenticated user info

**Headers:**

```
Authorization: Bearer {{accessToken}}
```

**Response:** (200 OK)

```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "fullName": "Test User",
  "role": "ROLE_USER"
}
```

**Error Responses:**

- `401` - Unauthorized (missing/invalid token)

---

### 4. Refresh Token

**Endpoint:**

```
POST /api/v1/auth/refresh
```

**Description:** Get new access token using refresh token

**Request:**

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Response:** (200 OK)

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "expiresIn": 86400000
}
```

**Error Responses:**

- `401` - Invalid/expired refresh token

---

### 5. Logout

**Endpoint:**

```
POST /api/v1/auth/logout
```

**Description:** Invalidate refresh token

**Headers:**

```
Authorization: Bearer {{accessToken}}
```

**Request:**

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Response:** (200 OK)

```json
{
  "message": "Đăng xuất thành công"
}
```

---

### 6. Forgot Password

**Endpoint:**

```
POST /api/v1/auth/forgot-password
```

**Description:** Request password reset token

**Request:**

```json
{
  "email": "test@example.com"
}
```

**Response:** (200 OK)

```json
{
  "message": "Reset token: abc123xyz789...",
  "resetToken": "abc123xyz789..."
}
```

**Error Responses:**

- `404` - User not found

---

### 7. Reset Password

**Endpoint:**

```
POST /api/v1/auth/reset-password
```

**Description:** Reset password with token

**Request:**

```json
{
  "token": "abc123xyz789...",
  "newPassword": "newpassword456"
}
```

**Response:** (200 OK)

```json
{
  "message": "Đặt lại mật khẩu thành công"
}
```

**Error Responses:**

- `400` - Invalid/expired token
- `404` - User not found

---

## **Movie APIs (Public)** (4 Endpoints)

Public endpoints - No authentication required.

### 8. List All Movies

**Endpoint:**

```
GET /api/v1/movies
```

**Description:** Get all published movies

**Response:** (200 OK)

```json
[
  {
    "id": 1,
    "title": "Avengers",
    "slug": "avengers",
    "posterUrl": "https://example.com/poster.jpg",
    "releaseYear": 2019,
    "country": "USA",
    "language": "English",
    "ageRating": "PG-13",
    "movieType": "SINGLE",
    "isPremiumOnly": false,
    "averageRating": 8.5,
    "viewCount": 1000
  }
]
```

---

### 9. Get Movie by ID

**Endpoint:**

```
GET /api/v1/movies/{id}
```

**Parameters:**

- `id` (path, required): Movie ID (must be positive)

**Response:** (200 OK)

```json
{
  "id": 1,
  "title": "Avengers",
  "originalTitle": "Avengers: Endgame",
  "slug": "avengers",
  "description": "Thanos threatens the entire universe...",
  "posterUrl": "https://example.com/poster.jpg",
  "bannerUrl": "https://example.com/banner.jpg",
  "trailerUrl": "https://example.com/trailer.mp4",
  "releaseYear": 2019,
  "country": "USA",
  "language": "English",
  "ageRating": "PG-13",
  "movieStatus": "PUBLISHED",
  "movieType": "SINGLE",
  "isPremiumOnly": false,
  "viewCount": 1000,
  "favoriteCount": 500,
  "averageRating": 8.5,
  "totalRatings": 250,
  "totalReviews": 150,
  "publishedAt": "2019-04-26T00:00:00",
  "episodes": [
    {
      "id": 1,
      "title": "Episode 1",
      "episodeNumber": 1,
      "videoUrl": "https://example.com/video1.mp4",
      "thumbnailUrl": "https://example.com/thumb1.jpg",
      "durationSeconds": 3600,
      "isFreePreview": true,
      "status": "PUBLISHED"
    }
  ]
}
```

**Error Responses:**

- `404` - Movie not found
- `400` - Invalid ID (negative/non-numeric)

---

### 10. Get Movie by Slug

**Endpoint:**

```
GET /api/v1/movies/slug/{slug}
```

**Parameters:**

- `slug` (path, required): Movie slug (URL-friendly name)

**Response:** (200 OK)
Same as Get Movie by ID response

**Error Responses:**

- `404` - Movie not found

---

### 11. Get Movie Episodes

**Endpoint:**

```
GET /api/v1/movies/{id}/episodes
```

**Parameters:**

- `id` (path, required): Movie ID

**Response:** (200 OK)

```json
[
  {
    "id": 1,
    "title": "Episode 1",
    "episodeNumber": 1,
    "videoUrl": "https://example.com/video1.mp4",
    "thumbnailUrl": "https://example.com/thumb1.jpg",
    "durationSeconds": 3600,
    "isFreePreview": true,
    "status": "PUBLISHED"
  },
  {
    "id": 2,
    "title": "Episode 2",
    "episodeNumber": 2,
    "videoUrl": "https://example.com/video2.mp4",
    "thumbnailUrl": "https://example.com/thumb2.jpg",
    "durationSeconds": 3600,
    "isFreePreview": false,
    "status": "PUBLISHED"
  }
]
```

---

## **Movie Admin APIs** (5 Endpoints)

Admin-only endpoints. Requires `ROLE_ADMIN` JWT token.

**Auth Headers (All Admin Endpoints):**

```
Authorization: Bearer {{adminAccessToken}}
Content-Type: application/json
```

### 12. Create Movie

**Endpoint:**

```
POST /api/v1/admin/movies
```

**Request:**

```json
{
  "title": "New Movie",
  "originalTitle": "Original Title",
  "slug": "new-movie-unique",
  "description": "Movie description...",
  "posterUrl": "https://example.com/poster.jpg",
  "bannerUrl": "https://example.com/banner.jpg",
  "trailerUrl": "https://example.com/trailer.mp4",
  "releaseYear": 2024,
  "country": "USA",
  "language": "English",
  "ageRating": "PG-13",
  "movieType": "SINGLE",
  "movieStatus": "DRAFT",
  "isPremiumOnly": false
}
```

**Response:** (200 OK)

```json
{
  "id": 10,
  "title": "New Movie",
  "slug": "new-movie-unique",
  "description": "Movie description...",
  "movieType": "SINGLE",
  "movieStatus": "DRAFT",
  "isPremiumOnly": false,
  "createdAt": "2024-04-06T21:00:00"
}
```

**Error Responses:**

- `409` - Slug already exists
- `403` - Forbidden (not admin)
- `400` - Validation error

---

### 13. Update Movie

**Endpoint:**

```
PUT /api/v1/admin/movies/{id}
```

**Parameters:**

- `id` (path, required): Movie ID

**Request:**

```json
{
  "title": "Updated Title",
  "description": "Updated description...",
  "posterUrl": "https://example.com/new-poster.jpg",
  "releaseYear": 2024,
  "movieType": "SERIES",
  "movieStatus": "PUBLISHED",
  "isPremiumOnly": true
}
```

**Response:** (200 OK)

```json
{
  "id": 10,
  "title": "Updated Title",
  "movieStatus": "PUBLISHED",
  "movieType": "SERIES",
  "isPremiumOnly": true,
  "publishedAt": "2024-04-06T21:05:00",
  "updatedAt": "2024-04-06T21:05:00"
}
```

**Error Responses:**

- `404` - Movie not found
- `403` - Forbidden
- `400` - Validation error

---

### 14. Delete Movie

**Endpoint:**

```
DELETE /api/v1/admin/movies/{id}
```

**Parameters:**

- `id` (path, required): Movie ID

**Response:** (204 No Content)

**Error Responses:**

- `404` - Movie not found
- `403` - Forbidden

---

### 15. Create Episode

**Endpoint:**

```
POST /api/v1/admin/movies/{id}/episodes
```

**Parameters:**

- `id` (path, required): Movie ID

**Request:**

```json
{
  "title": "Episode 1",
  "episodeNumber": 1,
  "videoUrl": "https://example.com/video1.mp4",
  "thumbnailUrl": "https://example.com/thumb1.jpg",
  "durationSeconds": 3600,
  "isFreePreview": true,
  "status": "PUBLISHED"
}
```

**Response:** (200 OK)

```json
{
  "id": 1,
  "title": "Episode 1",
  "episodeNumber": 1,
  "videoUrl": "https://example.com/video1.mp4",
  "thumbnailUrl": "https://example.com/thumb1.jpg",
  "durationSeconds": 3600,
  "isFreePreview": true,
  "status": "PUBLISHED",
  "createdAt": "2024-04-06T21:10:00"
}
```

**Error Responses:**

- `404` - Movie not found
- `403` - Forbidden
- `400` - Validation error

---

### 16. Delete Episode

**Endpoint:**

```
DELETE /api/v1/admin/movies/{id}/episodes/{episodeId}
```

**Parameters:**

- `id` (path, required): Movie ID
- `episodeId` (path, required): Episode ID

**Response:** (204 No Content)

**Error Responses:**

- `404` - Movie/Episode not found
- `403` - Forbidden

---

## **User APIs** (8 Endpoints)

### Personal User Endpoints (Requires Bearer Token)

**Auth Headers:**

```
Authorization: Bearer {{accessToken}}
Content-Type: application/json
```

### 17. Get My Profile

**Endpoint:**

```
GET /api/v1/users/me
```

**Response:** (200 OK)

```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "fullName": "Test User",
  "avatarUrl": "https://example.com/avatar.jpg",
  "role": "ROLE_USER",
  "accountStatus": "ACTIVE",
  "createdAt": "2024-01-01T00:00:00"
}
```

**Error Responses:**

- `401` - Unauthorized

---

### 18. Update My Profile

**Endpoint:**

```
PUT /api/v1/users/me
```

**Request:**

```json
{
  "fullName": "Updated Name",
  "email": "newemail@example.com",
  "avatarUrl": "https://example.com/new-avatar.jpg"
}
```

**Response:** (200 OK)

```json
{
  "id": 1,
  "username": "testuser",
  "email": "newemail@example.com",
  "fullName": "Updated Name",
  "avatarUrl": "https://example.com/new-avatar.jpg",
  "updatedAt": "2024-04-06T21:15:00"
}
```

**Error Responses:**

- `401` - Unauthorized
- `400` - Validation error

---

### 19. Change My Password

**Endpoint:**

```
PATCH /api/v1/users/me/password
```

**Request:**

```json
{
  "oldPassword": "password123",
  "newPassword": "newpassword456"
}
```

**Response:** (200 OK)

```json
{
  "message": "Đổi mật khẩu thành công"
}
```

**Error Responses:**

- `401` - Unauthorized
- `400` - Invalid old password

---

### Admin User Endpoints (Requires ADMIN Role)

**Auth Headers (All Admin Endpoints):**

```
Authorization: Bearer {{adminAccessToken}}
Content-Type: application/json
```

### 20. List All Users

**Endpoint:**

```
GET /api/v1/users
```

**Query Parameters:**

- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)

**Response:** (200 OK)

```json
[
  {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "fullName": "Test User",
    "role": "ROLE_USER",
    "accountStatus": "ACTIVE"
  },
  {
    "id": 2,
    "username": "admin",
    "email": "admin@example.com",
    "fullName": "Admin User",
    "role": "ROLE_ADMIN",
    "accountStatus": "ACTIVE"
  }
]
```

**Error Responses:**

- `403` - Forbidden (not admin)

---

### 21. Get User by ID

**Endpoint:**

```
GET /api/v1/users/{id}
```

**Parameters:**

- `id` (path, required): User ID

**Response:** (200 OK)

```json
{
  "id": 1,
  "username": "testuser",
  "email": "test@example.com",
  "fullName": "Test User",
  "avatarUrl": "https://example.com/avatar.jpg",
  "role": "ROLE_USER",
  "accountStatus": "ACTIVE",
  "premiumExpiryDate": null,
  "createdAt": "2024-01-01T00:00:00",
  "lastLoginAt": "2024-04-06T20:00:00"
}
```

**Error Responses:**

- `404` - User not found
- `403` - Forbidden

---

### 22. Update User Status

**Endpoint:**

```
PATCH /api/v1/users/{id}/status
```

**Parameters:**

- `id` (path, required): User ID

**Request:**

```json
{
  "status": "ACTIVE"
}
```

**Valid Status Values:**

- `ACTIVE` - User can login
- `BLOCKED` - User cannot login
- `DELETED` - User account deleted

**Response:** (200 OK)

```json
{
  "id": 1,
  "username": "testuser",
  "accountStatus": "BLOCKED",
  "updatedAt": "2024-04-06T21:20:00"
}
```

**Error Responses:**

- `404` - User not found
- `403` - Forbidden
- `400` - Invalid status

---

### 23. Update User Role

**Endpoint:**

```
PATCH /api/v1/users/{id}/role
```

**Parameters:**

- `id` (path, required): User ID

**Request:**

```json
{
  "role": "ROLE_ADMIN"
}
```

**Valid Role Values:**

- `ROLE_ADMIN` - Administrator
- `ROLE_USER` - Regular user

**Response:** (200 OK)

```json
{
  "id": 1,
  "username": "testuser",
  "role": "ROLE_ADMIN",
  "updatedAt": "2024-04-06T21:25:00"
}
```

**Error Responses:**

- `404` - User not found
- `403` - Forbidden
- `400` - Invalid role

---

### 24. Delete User

**Endpoint:**

```
DELETE /api/v1/users/{id}
```

**Parameters:**

- `id` (path, required): User ID

**Response:** (204 No Content)

**Error Responses:**

- `404` - User not found
- `403` - Forbidden

---

## Authentication

### JWT Token Structure

Access Token: Short-lived token (24 hours) used for API requests
Refresh Token: Long-lived token used to get new access tokens

### Using Tokens

**All protected endpoints require Bearer token:**

```bash
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
     http://localhost:8080/api/v1/users/me
```

### Token Refresh Flow

1. Access token expires
2. Use Refresh Token endpoint to get new token pair
3. Use new Access Token for subsequent requests

---

## Error Handling

### Standard Error Response

```json
{
  "timestamp": "2024-04-06T21:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/movies"
}
```

### HTTP Status Codes

| Status  | Meaning      | Common Cause              |
| ------- | ------------ | ------------------------- |
| **200** | OK           | Request succeeded         |
| **201** | Created      | Resource created          |
| **204** | No Content   | DELETE success            |
| **400** | Bad Request  | Invalid input             |
| **401** | Unauthorized | Missing/invalid token     |
| **403** | Forbidden    | No permission (not admin) |
| **404** | Not Found    | Resource doesn't exist    |
| **409** | Conflict     | Duplicate (slug exists)   |
| **500** | Server Error | Server error              |

### Common Error Codes

| Code                      | Message               | Solution                  |
| ------------------------- | --------------------- | ------------------------- |
| `USERNAME_ALREADY_EXISTS` | Username taken        | Choose different username |
| `EMAIL_ALREADY_EXISTS`    | Email taken           | Choose different email    |
| `MOVIE_NOT_FOUND`         | Movie doesn't exist   | Check movie ID            |
| `USER_NOT_FOUND`          | User doesn't exist    | Check user ID             |
| `MOVIE_SLUG_EXISTED`      | Slug already used     | Change movie slug         |
| `INVALID_CREDENTIALS`     | Wrong password        | Check credentials         |
| `INVALID_TOKEN`           | Token expired/invalid | Refresh token for new one |

---

## Request/Response Examples

### Example 1: Register and Login Flow

#### Step 1: Register

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "password123",
    "fullName": "New User"
  }'
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 3,
  "username": "newuser",
  "email": "newuser@example.com"
}
```

#### Step 2: Save tokens to environment

```bash
export ACCESS_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
export REFRESH_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Step 3: Use token in requests

```bash
curl http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer $ACCESS_TOKEN"
```

---

### Example 2: Movie Management Flow

#### Step 1: Get all movies (public)

```bash
curl http://localhost:8080/api/v1/movies
```

#### Step 2: Create movie (admin)

```bash
curl -X POST http://localhost:8080/api/v1/admin/movies \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Inception",
    "slug": "inception-2024",
    "description": "A mind-bending thriller",
    "releaseYear": 2024,
    "movieType": "SINGLE",
    "movieStatus": "DRAFT",
    "isPremiumOnly": false
  }'
```

#### Step 3: Add episodes

```bash
curl -X POST http://localhost:8080/api/v1/admin/movies/1/episodes \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Full Movie",
    "episodeNumber": 1,
    "videoUrl": "https://example.com/inception.mp4",
    "durationSeconds": 8820,
    "isFreePreview": false,
    "status": "PUBLISHED"
  }'
```

#### Step 4: Publish movie

```bash
curl -X PUT http://localhost:8080/api/v1/admin/movies/1 \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Inception",
    "description": "A mind-bending thriller",
    "releaseYear": 2024,
    "movieType": "SINGLE",
    "movieStatus": "PUBLISHED",
    "isPremiumOnly": false
  }'
```

#### Step 5: View as user (publicly available)

```bash
curl http://localhost:8080/api/v1/movies/1
```

---

## Testing

### Using Postman

1. Import `postman_collection.json`
2. Import `postman_environment.json`
3. Select environment "Movie Streaming API - Local"
4. Run requests in order

### Using cURL

```bash
# Set variables
BASE_URL="http://localhost:8080"
API_PREFIX="/api/v1"

# Register
curl -X POST $BASE_URL$API_PREFIX/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com","password":"pass123","fullName":"Test"}'

# Login
curl -X POST $BASE_URL$API_PREFIX/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"test","password":"pass123"}'

# Get profile
curl $BASE_URL$API_PREFIX/users/me \
  -H "Authorization: Bearer YOUR_TOKEN"

# List movies
curl $BASE_URL$API_PREFIX/movies
```

### Using Thunder Client / Insomnia

1. Create new collection
2. Add base URL: `http://localhost:8080`
3. Add requests for each endpoint
4. Use environment variables for tokens

---

## API Statistics

| Category        | Count  | Auth Required |
| --------------- | ------ | ------------- |
| Authentication  | 7      | Mixed         |
| Movies (Public) | 4      | No            |
| Movies (Admin)  | 5      | Yes (ADMIN)   |
| Users           | 8      | Yes (Mixed)   |
| **Total**       | **24** | -             |

---

## Database Schema

### Main Tables

- `users` - User accounts
- `movies` - Movie information
- `episodes` - Movie episodes
- `refresh_tokens` - JWT refresh tokens
- `password_reset_tokens` - Password reset tokens

### Relationships

```
Users ← Refresh Tokens
      ← Password Reset Tokens

Movies → Episodes
       → Ratings
       → Reviews
```

---

## Security Features

✅ **JWT Authentication** - Secure token-based auth
✅ **Role-Based Access** - ADMIN vs USER roles
✅ **Password Hashing** - BCrypt encryption
✅ **CORS Protected** - Cross-origin request handling
✅ **Input Validation** - Request validation on all endpoints
✅ **SQL Injection Prevention** - JPA parameterized queries
✅ **Rate Limiting Ready** - Can add rate limiting

---

## Performance Optimization

- Pagination support for list endpoints
- Index on frequently queried fields
- Lazy loading for relationships
- Query optimization with JPA projections

---

## Future Enhancements

- [ ] Advanced movie search with filters
- [ ] User ratings and reviews
- [ ] Wishlist functionality
- [ ] Watch history tracking
- [ ] Subtitle management
- [ ] Streaming quality options
- [ ] Payment integration
- [ ] Recommendation engine

---

## Troubleshooting

### Issue: "Access Denied" (401)

**Solution:**

1. Ensure token is set: `Authorization: Bearer TOKEN`
2. Check token not expired
3. Refresh token using `/auth/refresh` endpoint

### Issue: "Forbidden" (403)

**Solution:**

1. Verify user has ADMIN role
2. Check authentication is present
3. Confirm ADMIN token is used (not regular user token)

### Issue: "Movie Not Found" (404)

**Solution:**

1. Verify movie exists
2. Check movie is published (visible to users)
3. Use correct movie ID or slug

### Issue: "Slug Already Exists" (409)

**Solution:**

1. Use unique slug for each movie
2. Slugs are case-insensitive
3. Can't update existing movie slug

### Issue: Connection Refused

**Solution:**

1. Ensure MySQL is running
2. Check database port (default: 3306)
3. Verify database credentials in environment

---

## Support & Documentation

- API Docs: Available on `/swagger-ui.html` (if Swagger enabled)
- Issues: Report bugs with detailed error messages
- Postman Collection: Use provided collection for easy testing
- Environment: Use provided environment file for quick setup

---

## License

Proprietary - Movie Streaming Platform

---

**Last Updated:** April 6, 2026
**Version:** 1.0.0
**Status:** Preparing
