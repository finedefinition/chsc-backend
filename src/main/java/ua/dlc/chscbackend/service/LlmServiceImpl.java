package ua.dlc.chscbackend.service;

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

    public LlmServiceImpl(@Value("${openai.api.url}") String apiUrl, @Value("${openai.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public Mono<String> getChatResponse(String userMessage) {
        return this.webClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model", "gpt-4-turbo-preview",
                        "messages", List.of(Map.of("role", "user", "content", userMessage)),
                        "temperature", 0.7
                ))
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .maxBackoff(Duration.ofSeconds(10))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests));
    }
}

