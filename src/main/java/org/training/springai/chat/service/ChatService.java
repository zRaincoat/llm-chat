package org.training.springai.chat.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Service;
import org.training.springai.chat.config.ChatClientConfig;
import org.training.springai.chat.model.ChatRequest;
import org.training.springai.chat.model.ChatResponse;
import org.training.springai.chat.model.Personality;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatClient client;

  public ChatResponse call(ChatRequest request) {
    Personality personality = request.getPersonality();
    Instant start = Instant.now();

    org.springframework.ai.chat.model.ChatResponse aiResponse = client.prompt()
        .system(personality.getSystemPrompt())
        .user(request.getMessage())
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, ChatClientConfig.CONVERSATION_ID))
        .call()
        .chatResponse();

    double durationSeconds = Duration.between(start, Instant.now()).toMillis() / 1000.0;

    Generation generation = aiResponse != null ? aiResponse.getResult() : null;
    String reply = Optional.ofNullable(generation)
        .map(Generation::getOutput)
        .map(AbstractMessage::getText)
        .orElse("");
    String finishReason = Optional.ofNullable(generation)
        .map(Generation::getMetadata)
        .map(ChatGenerationMetadata::getFinishReason)
        .orElse(null);

    ChatResponseMetadata metadata = aiResponse != null ? aiResponse.getMetadata() : null;
    String model = metadata != null ? metadata.getModel() : null;
    Usage usage = metadata != null ? metadata.getUsage() : null;

    return ChatResponse.builder()
        .reply(reply)
        .personality(personality)
        .model(model)
        .finishReason(finishReason)
        .promptTokens(usage != null ? usage.getPromptTokens() : null)
        .completionTokens(usage != null ? usage.getCompletionTokens() : null)
        .totalTokens(usage != null ? usage.getTotalTokens() : null)
        .durationSeconds(durationSeconds)
        .build();
  }
}
