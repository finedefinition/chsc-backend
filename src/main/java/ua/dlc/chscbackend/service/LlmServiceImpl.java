package ua.dlc.chscbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ua.dlc.chscbackend.constants.ApplicationConstants;
import ua.dlc.chscbackend.dto.request.UserMessageRequestDto;
import ua.dlc.chscbackend.dto.response.CombinedResponseDto;
import ua.dlc.chscbackend.dto.response.ContentResponseDto;
import ua.dlc.chscbackend.model.News;
import ua.dlc.chscbackend.model.Ticker;
import ua.dlc.chscbackend.util.ChatMessageParser;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LlmServiceImpl implements LlmService{

    private final WebClient webClient;

    private final NewsService newsService;

    public LlmServiceImpl(@Value("${openai.api.url}") String apiUrl,
                          @Value("${openai.api.key}") String apiKey, NewsService newsService) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        this.newsService = newsService;
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

    public Mono<CombinedResponseDto> fetchChatAndNews(UserMessageRequestDto userMessageRequestDto) {
        Mono<ContentResponseDto> chatCompletionMono = getChatResponse(userMessageRequestDto.getContent())
                .map(content -> new ContentResponseDto(content));

        Mono<List<News>> latestNewsMono = getListMono(userMessageRequestDto);

        // Explicitly define the BiFunction for clarity and to ensure type matching
        return Mono.zip(chatCompletionMono, latestNewsMono,
                (contentResponseDto, newsList) -> new CombinedResponseDto(contentResponseDto, newsList));
    }


    public Mono<List<News>> getListMono(UserMessageRequestDto userMessageRequestDto) {
        // Extract ticker from the message
        Optional<Ticker> optionalTicker = ChatMessageParser.extractTicker(userMessageRequestDto.getContent());

        // If a ticker is identified, fetch the latest news, otherwise return empty list
        return optionalTicker.map(ticker ->
                Mono.just(newsService.getAllNews(ticker, ApplicationConstants.START_DATE,
                                ApplicationConstants.END_DATE))
                        .map(newsList -> newsList.stream().limit(5).collect(Collectors.toList()))
        ).orElse(Mono.just(Collections.emptyList()));
    }
}

