package org.training.springai.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

  private String reply;
  private Personality personality;
  private String model;
  private String finishReason;
  private Integer promptTokens;
  private Integer completionTokens;
  private Integer totalTokens;
  private double durationSeconds;
}
