package ua.dlc.chscbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class LlmServiceImpl implements LlmService{

    private final WebClient webClient;

    public LlmServiceImpl(@Value("${openai.api.url}") String apiUrl,
                          @Value("${openai.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public Mono<String> getChatResponse(String userMessage) {
        ObjectMapper objectMapper = new ObjectMapper(); // Ensure ObjectMapper is available

        return this.webClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", "gpt-4-turbo-preview",
                        "messages", List.of(Map.of("role", "user", "content", userMessage)),
                        "temperature", 0.7
                ))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode root = objectMapper.readTree(response);
                        JsonNode choices = root.path("choices");
                        if (!choices.isEmpty()) {
                            JsonNode firstChoice = choices.get(0);
                            JsonNode message = firstChoice.path("message");
                            String content = message.path("content").asText();
                            return content; // Extracted content
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // Log or handle parsing exceptions
                    }
                    return ""; // Return an empty string or some default response if parsing fails or content is not found
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests));
    }
}

