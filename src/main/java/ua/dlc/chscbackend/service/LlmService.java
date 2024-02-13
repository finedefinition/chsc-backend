package ua.dlc.chscbackend.service;

import reactor.core.publisher.Mono;
import ua.dlc.chscbackend.dto.request.UserMessageRequestDto;
import ua.dlc.chscbackend.model.News;

import java.util.List;

public interface LlmService {
    Mono<String> getChatResponse(String userMessage);

    Mono<List<News>> getListMono(UserMessageRequestDto userMessageRequestDto);
}
