package ua.dlc.chscbackend.controler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.dlc.chscbackend.constants.ApplicationConstants;
import ua.dlc.chscbackend.dto.response.ChatCompletionResponseDto;
import ua.dlc.chscbackend.dto.response.CombinedResponseDto;
import ua.dlc.chscbackend.dto.request.UserMessageRequestDto;
import ua.dlc.chscbackend.model.News;
import ua.dlc.chscbackend.model.Ticker;
import ua.dlc.chscbackend.service.LlmServiceImpl;
import ua.dlc.chscbackend.service.NewsService;
import ua.dlc.chscbackend.util.ChatMessageParser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class LlmController {

    private final LlmServiceImpl llmService;

    private final NewsService newsService;

    private final ObjectMapper objectMapper;

    public LlmController(LlmServiceImpl llmService, NewsService newsService, ObjectMapper objectMapper) {
        this.llmService = llmService;
        this.newsService = newsService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/ask", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> askLlm(@RequestBody UserMessageRequestDto userMessageRequestDto) {
        // Assuming UserMessage is a class that encapsulates the user's input
        return llmService.getChatResponse(userMessageRequestDto.getContent());
    }

    @PostMapping(value = "/askNews", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CombinedResponseDto> askLlmAndFetchNews(@RequestBody UserMessageRequestDto userMessageRequestDto) {
        // Extract ticker from the message
        Optional<Ticker> optionalTicker = ChatMessageParser.extractTicker(userMessageRequestDto.getContent());

        // Fetch ChatGPT completion
        Mono<ChatCompletionResponseDto> chatCompletionMono = llmService.getChatResponse(userMessageRequestDto.getContent())
                .handle((response, sink) -> {
                    try {
                        sink.next(objectMapper.readValue(response, ChatCompletionResponseDto.class));
                    } catch (JsonProcessingException e) {
                        sink.error(new RuntimeException("Error parsing JSON", e));
                    }
                });

        // If a ticker is identified, fetch the latest news, otherwise return empty list
        Mono<List<News>> latestNewsMono = optionalTicker.map(ticker ->
                Mono.just(newsService.getAllNews(ticker, ApplicationConstants.START_DATE,
                                ApplicationConstants.END_DATE))
                        .map(newsList -> newsList.stream().limit(5).collect(Collectors.toList())) // Assuming getAllNews returns sorted list
        ).orElse(Mono.just(Collections.emptyList()));

        // Combine both Monos into a CombinedResponse
        return Mono.zip(chatCompletionMono, latestNewsMono, CombinedResponseDto::new);
    }

}
