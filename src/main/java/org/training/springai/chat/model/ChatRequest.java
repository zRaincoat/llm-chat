package org.training.springai.chat.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

  @NotBlank(message = "message must not be blank")
  @Size(max = 2000, message = "message must be at most 2000 characters")
  private String message;

  private Personality personality = Personality.DEFAULT;
}
