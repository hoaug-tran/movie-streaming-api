package com.hoaug.movieapi.modules.chatbot.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hoaug.movieapi.modules.chatbot.domain.ChatMessage;
import com.hoaug.movieapi.modules.chatbot.domain.ChatRole;
import com.hoaug.movieapi.modules.chatbot.presentation.dto.ChatRequest;

@Service
public class ChatService {

  private static final Logger log = LoggerFactory.getLogger(ChatService.class);

  private final OllamaClient ollamaClient;
  private final PromptTemplates promptTemplates;
  private final ChatbotProperties properties;
  private final ChatContextBuilder contextBuilder;

  public ChatService(OllamaClient ollamaClient, PromptTemplates promptTemplates,
      ChatbotProperties properties, ChatContextBuilder contextBuilder) {
    this.ollamaClient = ollamaClient;
    this.promptTemplates = promptTemplates;
    this.properties = properties;
    this.contextBuilder = contextBuilder;
  }

  public void streamReply(ChatRequest request, ChatUserContext userContext,
      Consumer<String> onDelta) {
    List<ChatMessage> messages = buildMessages(request, userContext);
    try {
      ollamaClient.streamChat(messages, onDelta);
    } catch (IOException ex) {
      log.error("Ollama stream failed", ex);
      throw new ChatbotException("Không thể kết nối tới mô hình AI. Vui lòng thử lại sau.", ex);
    }
  }

  private List<ChatMessage> buildMessages(ChatRequest request, ChatUserContext userContext) {
    List<ChatMessage> messages = new ArrayList<>();

    StringBuilder systemPrompt = new StringBuilder(promptTemplates.baseSystemPrompt());
    systemPrompt.append("\n\n");
    if (userContext == null || !userContext.authenticated()) {
      systemPrompt.append(promptTemplates.guestContextHint());
    } else {
      systemPrompt.append(promptTemplates.userContextHint(userContext.displayName(),
          userContext.subscriptionTier()));
    }

    String contextBlock = contextBuilder.buildContextBlock(userContext);
    if (!contextBlock.isEmpty()) {
      systemPrompt.append(contextBlock);
    }

    messages.add(ChatMessage.system(systemPrompt.toString()));

    List<ChatRequest.ChatHistoryItem> history = request.history();
    if (history != null && !history.isEmpty()) {
      int max = properties.getMaxHistoryMessages();
      int start = Math.max(0, history.size() - max);
      for (int i = start; i < history.size(); i++) {
        ChatRequest.ChatHistoryItem item = history.get(i);
        if (item.content() == null || item.content().isBlank()) {
          continue;
        }
        ChatRole role = ollamaClient.toRole(item.role());
        if (role == ChatRole.SYSTEM) {
          continue;
        }
        messages.add(new ChatMessage(role, item.content(), null));
      }
    }

    messages.add(ChatMessage.user(request.message()));
    return messages;
  }
}
