package org.training.springai.chat.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.training.springai.chat.config.ChatClientConfig;
import org.training.springai.chat.model.ChatRequest;
import org.training.springai.chat.model.ChatResponse;
import org.training.springai.chat.service.ChatService;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

  private final ChatService chatService;
  private final ChatMemory chatMemory;

  @PostMapping
  public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
    return chatService.call(request);
  }

  @DeleteMapping("/history")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void clearHistory() {
    chatMemory.clear(ChatClientConfig.CONVERSATION_ID);
  }
}
