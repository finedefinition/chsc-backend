package ua.dlc.chscbackend.controler;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.dlc.chscbackend.dto.UserMessage;
import ua.dlc.chscbackend.service.LlmService;

@RestController
public class LlmController {

    private final LlmService llmService;

    public LlmController(LlmService llmService) {
        this.llmService = llmService;
    }

    @PostMapping(value = "/ask", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> askLlm(@RequestBody UserMessage userMessage) {
        // Assuming UserMessage is a class that encapsulates the user's input
        return llmService.getChatResponse(userMessage.getContent());
    }
}
