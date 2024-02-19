package ua.dlc.chscbackend.dto.response;

import lombok.Data;
import ua.dlc.chscbackend.model.News;

import java.util.List;

@Data
public class CombinedResponseDto {

    private ContentResponseDto contentResponseDto;

    private List<News> latestNews;

    public CombinedResponseDto(ContentResponseDto contentResponseDto, List<News> latestNews) {
        this.contentResponseDto = contentResponseDto;
        this.latestNews = latestNews;
    }
}

