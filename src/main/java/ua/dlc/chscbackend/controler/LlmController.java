package ua.dlc.chscbackend.controler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.dlc.chscbackend.dto.request.UserMessageRequestDto;
import ua.dlc.chscbackend.dto.response.ChatCompletionResponseDto;
import ua.dlc.chscbackend.dto.response.CombinedResponseDto;
import ua.dlc.chscbackend.model.News;
import ua.dlc.chscbackend.service.LlmServiceImpl;

import java.util.List;

@RestController
public class LlmController {

    private final LlmServiceImpl llmService;

    private final ObjectMapper objectMapper;

    public LlmController(LlmServiceImpl llmService, ObjectMapper objectMapper) {
        this.llmService = llmService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/ask", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> askLlm(@RequestBody UserMessageRequestDto userMessageRequestDto) {
        return llmService.getChatResponse(userMessageRequestDto.getContent());
    }

    @PostMapping(value = "/askNews", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CombinedResponseDto> askLlmAndFetchNews(@RequestBody UserMessageRequestDto userMessageRequestDto) {
        
        // Fetch ChatGPT completion
        Mono<ChatCompletionResponseDto> chatCompletionMono = llmService.getChatResponse(userMessageRequestDto.getContent())
                .handle((response, sink) -> {
                    try {
                        sink.next(objectMapper.readValue(response, ChatCompletionResponseDto.class));
                    } catch (JsonProcessingException e) {
                        sink.error(new RuntimeException("Error parsing JSON", e));
                    }
                });

        Mono<List<News>> latestNewsMono = llmService.getListMono(userMessageRequestDto);

        // Combine both Mono into a CombinedResponse
        return Mono.zip(chatCompletionMono, latestNewsMono, CombinedResponseDto::new);
    }
}
