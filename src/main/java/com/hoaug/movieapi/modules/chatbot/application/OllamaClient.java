package com.hoaug.movieapi.modules.chatbot.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.hoaug.movieapi.modules.chatbot.domain.ChatMessage;
import com.hoaug.movieapi.modules.chatbot.domain.ChatRole;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Component
public class OllamaClient {

  private static final Logger log = LoggerFactory.getLogger(OllamaClient.class);
  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  private final ChatbotProperties properties;
  private final OkHttpClient httpClient;
  private final ObjectMapper objectMapper;

  public OllamaClient(ChatbotProperties properties, ObjectMapper objectMapper) {
    this.properties = properties;
    this.objectMapper = objectMapper;
    this.httpClient = new OkHttpClient.Builder()
        .connectTimeout(Duration.ofSeconds(10))
        .readTimeout(Duration.ofSeconds(properties.getOllamaTimeoutSeconds()))
        .writeTimeout(Duration.ofSeconds(30))
        .callTimeout(0, TimeUnit.MILLISECONDS)
        .build();
  }

  public void streamChat(List<ChatMessage> messages, Consumer<String> onDelta) throws IOException {
    var ollamaMessages = messages.stream().map(this::toOllamaMessage).toList();

    Map<String, Object> payload = Map.of(
        "model", properties.getOllamaModel(),
        "messages", ollamaMessages,
        "stream", true,
        "options", Map.of(
            "temperature", 0.6,
            "num_predict", 512,
            "top_p", 0.9
        )
    );

    String json = objectMapper.writeValueAsString(payload);
    Request request = new Request.Builder()
        .url(properties.getOllamaBaseUrl() + "/api/chat")
        .post(RequestBody.create(json, JSON))
        .build();

    try (Response response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        String body = response.body() != null ? response.body().string() : "";
        log.error("Ollama returned {} - body: {}", response.code(), body);
        throw new IOException("Ollama returned status " + response.code());
      }

      ResponseBody body = response.body();
      if (body == null) {
        throw new IOException("Empty response body from Ollama");
      }

      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(body.byteStream(), StandardCharsets.UTF_8))) {
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.isBlank()) {
            continue;
          }
          try {
            JsonNode node = objectMapper.readTree(line);
            JsonNode messageNode = node.get("message");
            if (messageNode != null) {
              JsonNode contentNode = messageNode.get("content");
              if (contentNode != null && contentNode.isTextual()) {
                String token = contentNode.asText();
                if (!token.isEmpty()) {
                  onDelta.accept(token);
                }
              }
            }
            if (node.has("done") && node.get("done").asBoolean(false)) {
              break;
            }
          } catch (Exception parseEx) {
            log.warn("Cannot parse Ollama line: {}", line, parseEx);
          }
        }
      }
    }
  }

  public boolean isHealthy() {
    Request request = new Request.Builder()
        .url(properties.getOllamaBaseUrl() + "/api/tags")
        .get()
        .build();
    try (Response response = httpClient.newCall(request).execute()) {
      return response.isSuccessful();
    } catch (IOException e) {
      return false;
    }
  }

  private Map<String, String> toOllamaMessage(ChatMessage message) {
    String role = switch (message.role()) {
      case SYSTEM -> "system";
      case USER -> "user";
      case ASSISTANT -> "assistant";
      case TOOL -> "tool";
    };
    return Map.of("role", role, "content", message.content() == null ? "" : message.content());
  }

  public ChatRole toRole(String value) {
    if (value == null) {
      return ChatRole.USER;
    }
    return switch (value.toLowerCase()) {
      case "assistant", "bot" -> ChatRole.ASSISTANT;
      case "system" -> ChatRole.SYSTEM;
      case "tool" -> ChatRole.TOOL;
      default -> ChatRole.USER;
    };
  }
}
