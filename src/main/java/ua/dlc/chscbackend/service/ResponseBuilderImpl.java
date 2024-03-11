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

    private final GoogleChatService googleChatService;

    private final LlmService llmService;

    public ResponseBuilderImpl(NewsService newsService,
                               GoogleChatService googleChatService,
                               LlmService llmService) {
        this.newsService = newsService;
        this.googleChatService = googleChatService;
        this.llmService = llmService;
    }

    @Override
    public Mono<CombinedResponseDto> combineGoogleAndNewsResponse(Ticker ticker, LocalDateTime firstDate, LocalDateTime lastDate) {
        // Assuming getAllNews is a blocking call and returns List<News>
        // Wrap the synchronous operation in Mono.fromCallable to make it reactive
        Mono<List<News>> newsItemsMono = Mono.fromCallable(() -> newsService.getAllNews(ticker, firstDate, lastDate));

        // Assuming getChatResponse returns Mono<String>
        Mono<String> chatResponseMono = googleChatService.getChatResponse(
                String.format("I'm interested in the price of %s." +
                        " Can you provide me with a summary of the price movements" +
                        " from %s to %s", ticker, firstDate.toString(), lastDate.toString()));

        // Combine both Monos into a CombinedResponseDto
        return Mono.zip(chatResponseMono, newsItemsMono, (content, newsItems) -> {
            ContentResponseDto contentResponseDto = new ContentResponseDto(content);
            return new CombinedResponseDto(contentResponseDto, newsItems);
        });
    }
}
