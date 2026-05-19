package com.hoaug.movieapi.modules.chatbot.presentation.controller;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import tools.jackson.databind.ObjectMapper;
import com.hoaug.movieapi.modules.chatbot.application.ChatService;
import com.hoaug.movieapi.modules.chatbot.application.ChatUserContext;
import com.hoaug.movieapi.modules.chatbot.application.ChatUserContextProvider;
import com.hoaug.movieapi.modules.chatbot.application.ChatbotException;
import com.hoaug.movieapi.modules.chatbot.application.ChatbotProperties;
import com.hoaug.movieapi.modules.chatbot.application.OllamaClient;
import com.hoaug.movieapi.modules.chatbot.presentation.dto.ChatChunk;
import com.hoaug.movieapi.modules.chatbot.presentation.dto.ChatRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.prefix:/api/v1}/chat")
public class ChatController {

  private static final Logger log = LoggerFactory.getLogger(ChatController.class);

  private final ChatService chatService;
  private final ChatUserContextProvider userContextProvider;
  private final OllamaClient ollamaClient;
  private final ChatbotProperties properties;
  private final ObjectMapper objectMapper;
  private final Executor streamExecutor;

  public ChatController(ChatService chatService, ChatUserContextProvider userContextProvider,
      OllamaClient ollamaClient, ChatbotProperties properties, ObjectMapper objectMapper) {
    this.chatService = chatService;
    this.userContextProvider = userContextProvider;
    this.ollamaClient = ollamaClient;
    this.properties = properties;
    this.objectMapper = objectMapper;
    this.streamExecutor = Executors.newCachedThreadPool(runnable -> {
      Thread thread = new Thread(runnable);
      thread.setName("chatbot-stream-" + thread.getId());
      thread.setDaemon(true);
      return thread;
    });
  }

  @GetMapping("/health")
  public HealthResponse health() {
    return new HealthResponse(ollamaClient.isHealthy(), properties.getOllamaModel());
  }

  @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter stream(@Valid @RequestBody ChatRequest request, Authentication authentication) {
    long timeoutMs = properties.getOllamaTimeoutSeconds() * 1000L + 10_000L;
    SseEmitter emitter = new SseEmitter(timeoutMs);
    ChatUserContext userContext = userContextProvider.resolve(authentication);

    streamExecutor.execute(() -> {
      try {
        chatService.streamReply(request, userContext, token -> {
          try {
            emitter.send(SseEmitter.event()
                .name("message")
                .data(objectMapper.writeValueAsString(ChatChunk.delta(token)),
                    MediaType.APPLICATION_JSON));
          } catch (IOException sendEx) {
            throw new ChatbotException("Stream client disconnected", sendEx);
          }
        });
        emitter.send(SseEmitter.event()
            .name("message")
            .data(objectMapper.writeValueAsString(ChatChunk.done()),
                MediaType.APPLICATION_JSON));
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
    return emitter;
  }

  private void sendErrorAndComplete(SseEmitter emitter, String message) {
    try {
      emitter.send(SseEmitter.event()
          .name("message")
          .data(objectMapper.writeValueAsString(ChatChunk.error(message)),
              MediaType.APPLICATION_JSON));
    } catch (IOException ignored) {
    } finally {
      emitter.complete();
    }
  }

  public record HealthResponse(boolean ollamaReady, String model) {}
}
