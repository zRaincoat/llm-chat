# spring-ai

A small Spring Boot 4 + Spring AI demo that talks to a **local** Ollama model with conversation memory and selectable personalities.

## Prerequisites

1. **Install Ollama** → https://ollama.com/download
2. Pull the model used by this app:
   ```powershell
   ollama pull qwen3:8b
   ```
3. Make sure Ollama is running (it usually starts as a service after install):
   ```powershell
   ollama list
   ```

The model and Ollama URL are configured in `src/main/resources/application.yml`.

## Hardware

**Confirmed working on:**
Laptop with RTX 5060 (8 GB VRAM) + 32 GB RAM, Windows 11 — `qwen3:8b` runs fully on GPU at comfortable speed.

**Recommended (ideal):**
- GPU with **≥ 12 GB VRAM** so you can comfortably run an 8B–14B model with a larger context window, or step up to a 30B MoE like `qwen3:30b`.
- **32 GB system RAM** (16 GB works for 8B models, but tight).
- An NVMe SSD — model weights are several GB and load on first request.

**Minimum that's still usable:**
- 8 GB VRAM (what I have): stick to 7B–8B Q4 models, keep `num_ctx` ≤ 8192.
- CPU-only is technically possible but expect 2–5 tok/s on an 8B model — workable for testing, not for chat.

To swap models, change one line in `application.yml`:
```yaml
spring:
  ai:
    ollama:
      chat:
        options:
          model: qwen3:8b   # or llama3.1:8b, qwen2.5-coder:7b, gemma3:4b, ...
```

## Run

```powershell
./gradlew bootRun
```

App starts on `http://localhost:8080`.

## Request example

```powershell
curl -X POST http://localhost:8080/api/v1/chat `
  -H "Content-Type: application/json" `
  -d '{ "message": "Why is the sky blue?", "personality": "PIRATE" }'
```

Response:
```json
{
  "reply": "Arrr, matey! 'Tis Rayleigh scatterin' — the wee blue light from the sun bounces off the air more than the red...",
  "personality": "PIRATE",
  "model": "qwen3:8b",
  "finishReason": "stop",
  "promptTokens": 48,
  "completionTokens": 92,
  "totalTokens": 140,
  "durationSeconds": 3.412
}
```

### Personalities
`DEFAULT`, `PIRATE`, `SHAKESPEARE`, `GRUMPY` — defined in `Personality.java`. Add more by adding an enum entry with a system prompt.

### Conversation memory
History is kept in-memory for the lifetime of the JVM (single shared conversation, 20-message sliding window). To reset:

```powershell
curl -X DELETE http://localhost:8080/api/v1/chat/history
```
