package ua.dlc.chscbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ua.dlc.chscbackend.model.News;

import java.util.List;

@Data
@AllArgsConstructor
public class CombinedResponseDto {

    private ChatCompletionResponseDto chatCompletion;

    private List<News> latestNews;

}

