package ua.dlc.chscbackend.controler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.dlc.chscbackend.dto.request.UserMessageRequestDto;
import ua.dlc.chscbackend.service.LlmServiceImpl;

@RestController
public class LlmController {

    private final LlmServiceImpl llmService;


    public LlmController(LlmServiceImpl llmService) {
        this.llmService = llmService;
    }

    @PostMapping(value = "/ask", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> askLlm(@RequestBody UserMessageRequestDto userMessageRequestDto) {
        return llmService.getChatResponse(userMessageRequestDto.getContent());
    }
}
