package ua.dlc.chscbackend.service;

import reactor.core.publisher.Mono;
import ua.dlc.chscbackend.dto.response.CombinedResponseDto;
import ua.dlc.chscbackend.model.Ticker;

import java.time.LocalDateTime;

public interface ResponseBuilder {

    Mono<CombinedResponseDto> combineLlmAndNewsResponse(Ticker ticker, LocalDateTime firstDate, LocalDateTime lastDate);

}
