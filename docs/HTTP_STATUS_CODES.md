# HTTP STATUS CODES IMPLEMENTATION GUIDE

## Overview

All API endpoints must return proper HTTP status codes according to REST conventions.
Use the `ResponseUtil` helper class to ensure consistency.

## Status Code Mappings

### Success Responses

| Code | Status     | Usage                             | Example                                          |
| ---- | ---------- | --------------------------------- | ------------------------------------------------ |
| 200  | OK         | GET, PUT, PATCH (with response)   | `ResponseUtil.ok(data)`                          |
| 201  | Created    | POST (creates resource)           | `ResponseUtil.created(data, "/api/movies/{id}")` |
| 204  | No Content | DELETE, POST/PUT with no response | `ResponseUtil.noContent()`                       |

### Client Error Responses

| Code | Status               | Usage                          | Example                                                           |
| ---- | -------------------- | ------------------------------ | ----------------------------------------------------------------- |
| 400  | Bad Request          | Invalid request format/data    | `ResponseUtil.badRequest("Invalid email format")`                 |
| 401  | Unauthorized         | Authentication required/failed | `ResponseUtil.unauthorized("Invalid credentials")`                |
| 403  | Forbidden            | Insufficient permissions       | `ResponseUtil.forbidden("Admin role required")`                   |
| 404  | Not Found            | Resource doesn't exist         | `ResponseUtil.notFound("Movie not found")`                        |
| 409  | Conflict             | Resource already exists        | `ResponseUtil.conflict("Email already registered")`               |
| 422  | Unprocessable Entity | Valid syntax, semantic error   | `ResponseUtil.unprocessable("Cannot cancel active subscription")` |

### Server Error Responses

| Code | Status         | Usage                   | Example                                |
| ---- | -------------- | ----------------------- | -------------------------------------- |
| 500  | Internal Error | Unexpected server error | Auto-handled by GlobalExceptionHandler |

---

## Controller Implementation Examples

### CREATE (POST) - Returns 201 Created

```java
@PostMapping
public ResponseEntity<?> create(
    @Valid @RequestBody CreateMovieRequest request) {
    MovieResponse created = movieService.create(request);
    // Returns 201 with Location header to new resource
    return ResponseUtil.created(created, "/api/v1/movies/" + created.getId());
}
```

### READ (GET) - Returns 200 OK

```java
@GetMapping("/{id}")
public ResponseEntity<MovieResponse> getById(@PathVariable Long id) {
    MovieResponse movie = movieService.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    // Returns 200 OK
    return ResponseUtil.ok(movie);
}
```

### UPDATE (PUT) - Returns 200 OK

```java
@PutMapping("/{id}")
public ResponseEntity<MovieResponse> update(
    @PathVariable Long id,
    @Valid @RequestBody UpdateMovieRequest request) {
    MovieResponse updated = movieService.update(id, request);
    // Returns 200 OK
    return ResponseUtil.ok(updated);
}
```

### DELETE (DELETE) - Returns 204 No Content

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    movieService.delete(id);
    // or:
    // movieService.deleteOrThrow(id, ErrorCode.MOVIE_NOT_FOUND);
    // Returns 204 No Content (no body)
    return ResponseUtil.noContent();
}
```

### ERROR: Not Found - Returns 404

```java
@GetMapping("/{id}")
public ResponseEntity<MovieResponse> getById(@PathVariable Long id) {
    MovieResponse movie = movieService.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    // Throws AppException → GlobalExceptionHandler catches → Returns 404
    return ResponseUtil.ok(movie);
}
```

### ERROR: Validation - Returns 400

```java
@PostMapping
public ResponseEntity<?> create(
    @Valid @RequestBody CreateMovieRequest request) {
    // If validation fails on @Valid, GlobalExceptionHandler catches
    // MethodArgumentNotValidException → Returns 400 with field errors
}
```

### ERROR: Conflict (Duplicate) - Returns 409

```java
@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
    if (userService.existsByEmail(request.getEmail())) {
        throw new AppException(ErrorCode.EMAIL_EXISTED); // 409 Conflict
    }
    return ResponseUtil.created(userService.register(request));
}
```

### ERROR: Insufficient Permissions - Returns 403

```java
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{commentId}")
public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
    // If user lacks ADMIN role:
    // AccessDeniedException → GlobalExceptionHandler → Returns 403
    commentService.delete(commentId);
    return ResponseUtil.noContent();
}
```

---

## Current Status: Phase 1.4 Implementation

### Controllers Updated (Sample)

- [x] ResponseUtil class created
- [ ] WatchHistoryController - example update below
- [ ] AuthController
- [ ] AdminMovieController
- [ ] MovieController
- [ ] All other 20+ controllers (In Phase 2)

### Example: WatchHistoryController with Proper Status Codes

```java
@RestController
@RequestMapping("${api.prefix:/api/v1}/watch-histories")
public class WatchHistoryController {

    @PostMapping
    public ResponseEntity<?> upsert(
        Authentication authentication,
        @Valid @RequestBody UpsertWatchHistoryRequest request) {
        Long userId = getCurrentUserId(authentication);
        WatchHistoryResponse response = upsertWatchHistoryUseCase.execute(userId, request);
        // 201 Created for first time, 200 OK for updates (handled by service)
        return ResponseUtil.created(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyWatchHistories(Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        List<WatchHistoryResponse> histories =
            getMyWatchHistoriesUseCase.execute(userId);
        return ResponseUtil.ok(histories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        watchHistoryService.delete(id);
        return ResponseUtil.noContent(); // 204
    }
}
```
