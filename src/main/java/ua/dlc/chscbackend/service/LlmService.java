package ua.dlc.chscbackend.service;

import reactor.core.publisher.Mono;

public interface LlmService {
    Mono<String> getChatResponse(String userMessage);
}
