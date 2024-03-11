package ua.dlc.chscbackend.service;

import reactor.core.publisher.Mono;

public interface GoogleChatService {
    Mono<String> getChatResponse(String userMessage);
}
