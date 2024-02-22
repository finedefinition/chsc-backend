package ua.dlc.chscbackend.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ua.dlc.chscbackend.dto.response.CombinedResponseDto;
import ua.dlc.chscbackend.dto.response.ContentResponseDto;
import ua.dlc.chscbackend.model.News;
import ua.dlc.chscbackend.model.Ticker;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResponseBuilderImpl implements ResponseBuilder {

    private final NewsService newsService;

    private final LlmService llmService;

    public ResponseBuilderImpl(NewsService newsService, LlmService llmService) {
        this.newsService = newsService;
        this.llmService = llmService;
    }

    @Override
    public Mono<CombinedResponseDto> combineLlmAndNewsResponse(Ticker ticker, LocalDateTime firstDate, LocalDateTime lastDate) {
        // Assuming getAllNews is a blocking call and returns List<News>
        // Wrap the synchronous operation in Mono.fromCallable to make it reactive
        Mono<List<News>> newsItemsMono = Mono.fromCallable(() -> newsService.getAllNews(ticker, firstDate, lastDate));

        // Assuming getChatResponse returns Mono<String>
        Mono<String> chatResponseMono = llmService.getChatResponse(
                String.format("What happened to the price of %s from %s to %s", ticker, firstDate.toString(), lastDate.toString()));

        // Combine both Monos into a CombinedResponseDto
        return Mono.zip(chatResponseMono, newsItemsMono, (content, newsItems) -> {
            ContentResponseDto contentResponseDto = new ContentResponseDto(content);
            return new CombinedResponseDto(contentResponseDto, newsItems);
        });
    }
}
