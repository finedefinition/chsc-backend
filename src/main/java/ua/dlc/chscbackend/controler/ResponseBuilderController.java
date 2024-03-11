package ua.dlc.chscbackend.controler;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ua.dlc.chscbackend.dto.response.CombinedResponseDto;
import ua.dlc.chscbackend.model.Ticker;
import ua.dlc.chscbackend.service.ResponseBuilder;

import java.time.LocalDateTime;

@RestController
public class ResponseBuilderController {

    private final ResponseBuilder responseBuilder;

    public ResponseBuilderController(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @GetMapping("/response")
    public Mono<ResponseEntity<CombinedResponseDto>> getResponseAndNews(
            @RequestParam Ticker ticker,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime firstDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastDate) {

        Mono<CombinedResponseDto> combinedResponseDtoMono = responseBuilder
                .combineGoogleAndNewsResponse(ticker, firstDate, lastDate);

        // Use the combinedResponseDtoMono to create a Mono<ResponseEntity<CombinedResponseDto>>
        return combinedResponseDtoMono
                .map(ResponseEntity::ok) // Wrap the CombinedResponseDto in a ResponseEntity with an OK status
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Provide a default if the Mono is empty
    }


}
