package com.hoaug.movieapi.modules.chatbot.presentation.controller;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.hoaug.movieapi.modules.chatbot.application.ChatRateLimitException;
import com.hoaug.movieapi.modules.chatbot.application.ChatRateLimiter;
import com.hoaug.movieapi.modules.chatbot.application.ChatService;
import com.hoaug.movieapi.modules.chatbot.application.ChatUserContext;
import com.hoaug.movieapi.modules.chatbot.application.ChatUserContextProvider;
import com.hoaug.movieapi.modules.chatbot.application.ChatbotException;
import com.hoaug.movieapi.modules.chatbot.application.ChatbotProperties;
import com.hoaug.movieapi.modules.chatbot.application.OllamaClient;
import com.hoaug.movieapi.modules.chatbot.presentation.dto.ChatChunk;
import com.hoaug.movieapi.modules.chatbot.presentation.dto.ChatRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("${api.prefix:/api/v1}/assistant")
public class ChatController {

  private static final Logger log = LoggerFactory.getLogger(ChatController.class);

  private final ChatService chatService;
  private final ChatUserContextProvider userContextProvider;
  private final OllamaClient ollamaClient;
  private final ChatbotProperties properties;
  private final ChatRateLimiter rateLimiter;
  private final ObjectMapper objectMapper;
  private final Executor streamExecutor;

  public ChatController(ChatService chatService, ChatUserContextProvider userContextProvider,
      OllamaClient ollamaClient, ChatbotProperties properties, ChatRateLimiter rateLimiter,
      ObjectMapper objectMapper) {
    this.chatService = chatService;
    this.userContextProvider = userContextProvider;
    this.ollamaClient = ollamaClient;
    this.properties = properties;
    this.rateLimiter = rateLimiter;
    this.objectMapper = objectMapper;
    this.streamExecutor = new ThreadPoolExecutor(2, 16, 30L, TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(64), runnable -> {
          Thread thread = new Thread(runnable);
          thread.setName("chatbot-stream-" + thread.threadId());
          thread.setDaemon(true);
          return thread;
        }, new ThreadPoolExecutor.CallerRunsPolicy());
  }

  @GetMapping("/health")
  @PreAuthorize("hasRole('ADMIN')")
  public HealthResponse health () {
    return new HealthResponse(ollamaClient.isHealthy());
  }

  @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<SseEmitter> stream (@Valid @RequestBody ChatRequest request,
      Authentication authentication, HttpServletRequest httpRequest) {
    ChatUserContext userContext = userContextProvider.resolve(authentication);

    try {
      rateLimiter.enforce(userContext, resolveClientIp(httpRequest));
    } catch (ChatRateLimitException ex) {
      log.info("Chat rate limit hit: {}", ex.getMessage());
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    long timeoutMs = properties.getOllamaTimeoutSeconds() * 1000L + 10_000L;
    SseEmitter emitter = new SseEmitter(timeoutMs);

    streamExecutor.execute( () -> {
      try {
        chatService.streamReply(request, userContext, token -> {
          try {
            emitter.send(SseEmitter.event().name("message").data(
                objectMapper.writeValueAsString(ChatChunk.delta(token)),
                MediaType.APPLICATION_JSON));
          } catch (IOException sendEx) {
            throw new ChatbotException("Stream client disconnected", sendEx);
          }
        });
        emitter.send(SseEmitter.event().name("message")
            .data(objectMapper.writeValueAsString(ChatChunk.done()), MediaType.APPLICATION_JSON));
        emitter.complete();
      } catch (ChatbotException ex) {
        log.warn("Chat stream interrupted: {}", ex.getMessage());
        sendErrorAndComplete(emitter, ex.getMessage());
      } catch (Exception ex) {
        log.error("Unexpected chat stream failure", ex);
        sendErrorAndComplete(emitter, "Có lỗi xảy ra khi xử lý hội thoại. Vui lòng thử lại sau.");
      }
    });

    emitter.onTimeout(emitter::complete);
    emitter.onError(throwable -> log.warn("SSE error: {}", throwable.getMessage()));
    return ResponseEntity.ok(emitter);
  }

  private void sendErrorAndComplete (SseEmitter emitter, String message) {
    try {
      emitter.send(SseEmitter.event().name("message").data(
          objectMapper.writeValueAsString(ChatChunk.error(message)), MediaType.APPLICATION_JSON));
    } catch (IOException ignored) {
    } finally {
      emitter.complete();
    }
  }

  private String resolveClientIp (HttpServletRequest request) {
    String forwarded = request.getHeader("X-Forwarded-For");
    if (forwarded != null && !forwarded.isBlank()) {
      int comma = forwarded.indexOf(',');
      return (comma > 0 ? forwarded.substring(0, comma) : forwarded).trim();
    }
    String realIp = request.getHeader("X-Real-IP");
    if (realIp != null && !realIp.isBlank()) {
      return realIp.trim();
    }
    return request.getRemoteAddr();
  }

  public record HealthResponse(boolean ollamaReady) {
  }
}
