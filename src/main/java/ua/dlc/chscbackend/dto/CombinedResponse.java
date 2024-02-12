package ua.dlc.chscbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.dlc.chscbackend.model.News;

import java.util.List;

@Data
@AllArgsConstructor
public class CombinedResponse {
    private ChatCompletionResponse chatCompletion;
    private List<News> latestNews;

}

