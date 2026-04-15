# Redis Cache Implementation Guide

## Overview

Redis cache has been implemented for Phase 1 optimization with 3 cache layers:

- **movies**: Movie metadata cache (24h TTL)
- **searchResults**: Search results cache (1h TTL)
- **recommendations**: User recommendations cache (4h TTL)

## Setup

### 1. Docker Compose (Recommended for Local Development)

```bash
docker-compose up -d redis
```

This will start Redis on `localhost:6379`

### 2. Manual Installation (Alternative)

**Windows:**

```bash
# Using WSL2 or native Redis
choco install redis-cli
# or download from https://github.com/microsoftarchive/redis/releases
```

**macOS:**

```bash
brew install redis
brew services start redis
```

**Linux:**

```bash
sudo apt-get install redis-server
sudo systemctl start redis-server
```

### 3. Configuration

Configuration is in `application.properties` with environment variable overrides:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=
spring.data.redis.timeout=2000ms
spring.data.redis.jedis.pool.max-active=8
spring.data.redis.jedis.pool.max-idle=8
spring.data.redis.jedis.pool.min-idle=0
spring.data.redis.jedis.pool.max-wait=-1ms
```

For **local development**, use `application-local.properties` with identical settings.

## Cache Strategy

### What's Cached (Phase 1)

| Cache Name          | Data                     | TTL | Purpose                                  |
| ------------------- | ------------------------ | --- | ---------------------------------------- |
| **movies**          | Published movies list    | 24h | Frequently accessed movie metadata       |
| **searchResults**   | User search queries      | 1h  | Popular search patterns                  |
| **recommendations** | Per-user recommendations | 4h  | AI-generated or computed recommendations |

### Invalidation Strategy

Caches are automatically invalidated when:

- Movie status changes to PUBLISHED → `movies` cache cleared
- User recommendations are generated/deleted → `recommendations` cache cleared for that user
- Search cache expires after 1 hour

## Affected Use Cases

### Applied Cache Annotations

1. **GetMoviesUseCase** - `@Cacheable("movies")`
   - Caches all published movies
   - Key: `all_published_movies`

2. **SearchMovieUseCase** - `@Cacheable("searchResults")`
   - Caches search results per query
   - Key: `keyword:page:size:sortBy:sortDirection`

3. **AdvancedSearchMovieUseCase** - `@Cacheable("searchResults")`
   - Caches advanced search with filters
   - Key includes: keyword, pagination, year range, min rating, sort

4. **GetMyRecommendationsUseCase** - `@Cacheable("recommendations")`
   - Caches user recommendations
   - Key: `user:{userId}:recommendations`

5. **GenerateRecommendationsUseCase** - `@CacheEvict("recommendations")`
   - Clears cache when generating new recommendations

6. **ClearUserRecommendationsUseCase** - `@CacheEvict("recommendations")`
   - Clears cache when user clears recommendations

7. **DeleteMovieRecommendationUseCase** - `@CacheEvict("recommendations")`
   - Clears cache when deleting a recommendation

8. **UpdateMovieStatusUseCase** - `@CacheEvict("movies")` (conditional)
   - Clears movies cache when movie is PUBLISHED

## Files Modified

### Configuration

- **pom.xml** - Added Redis dependencies
- **application.properties** - Added Redis connection config
- **application-local.properties** - Added Redis local config
- **docker-compose.yml** - Added Redis service
- **common/config/CacheConfig.java** - Created Spring Cache configuration

### Use Cases (with @Cacheable/@CacheEvict)

- modules/movie/application/usecase/GetMoviesUseCase.java
- modules/movie/application/usecase/SearchMovieUseCase.java
- modules/movie/application/usecase/AdvancedSearchMovieUseCase.java
- modules/movie/application/usecase/UpdateMovieStatusUseCase.java
- modules/recommendation/application/usecase/GetMyRecommendationsUseCase.java
- modules/recommendation/application/usecase/GenerateRecommendationsUseCase.java
- modules/recommendation/application/usecase/ClearUserRecommendationsUseCase.java
- modules/recommendation/application/usecase/DeleteMovieRecommendationUseCase.java

## Testing Redis Connection

### Check Redis Connection

```bash
redis-cli ping
# Response: PONG
```

### View Cached Keys

```bash
redis-cli
> keys *
> get "all_published_movies"
```

### Clear All Cache

```bash
redis-cli
> FLUSHALL
```

## Running the Application

### With Docker

```bash
docker-compose up -d
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=local
```

### Without Docker

Ensure Redis is running separately, then:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=local
```

## Monitoring Cache Performance

### Using Spring Boot Actuator

Enable metrics endpoint in `application.properties`:

```properties
management.endpoints.web.exposure.include=health,info,metrics,caches
```

Then access:

```
http://localhost:8080/actuator/caches
http://localhost:8080/actuator/metrics/cache.hits
http://localhost:8080/actuator/metrics/cache.misses
```

### Redis CLI Monitoring

```bash
redis-cli
> INFO stats
> MONITOR
```

## Phase 2 Future Improvements

When ready, add caching for:

- User subscription status (15min TTL)
- Favorites/watchlist (5min TTL)
- User preferences
- Trending/featured sections

## Troubleshooting

### Redis Connection Refused

```
Connection refused at localhost:6379
```

**Solution:** Ensure Redis is running

- Docker: `docker-compose up -d redis`
- Manual: Start Redis service

### Cache Not Working

1. Verify `@EnableCaching` in CacheConfig.java
2. Check Redis connection in logs
3. Ensure Jedis dependency is in pom.xml

### Clear Cache If Needed

```bash
redis-cli FLUSHDB  # Clear current database
redis-cli FLUSHALL # Clear all databases
```

## Performance Notes

- **Cache Hit**: ~1-5ms response time
- **Cache Miss**: ~50-200ms (database query)
- **Expected Improvement**: 70-80% reduction in DB queries
- **Memory Usage**: ~50MB for typical 1000 movies dataset

## Architecture Notes

- Uses **Spring Cache abstraction** (compatible with any cache provider)
- Uses **Jedis client** for Redis connection pooling
- Uses **JSON serialization** for cache values
- Follows **clean architecture** - cache logic isolated in CacheConfig
- **Automatic TTL expiration** - no manual cleanup needed
