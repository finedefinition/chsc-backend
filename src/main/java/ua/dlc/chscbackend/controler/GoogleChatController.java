package ua.dlc.chscbackend.controler;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.dlc.chscbackend.service.GoogleChatService;

@RestController
public class GoogleChatController {

    private final GoogleChatService googleChatService;

    public GoogleChatController(GoogleChatService googleChatService) {
        this.googleChatService = googleChatService;
    }

    @PostMapping(value = "/askGoogle", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> askGoogle(@RequestBody String text) {
        return googleChatService.getChatResponse(text);
    }
}
