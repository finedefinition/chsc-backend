package ua.dlc.chscbackend.dto.response;

import lombok.Data;
import ua.dlc.chscbackend.model.News;


import java.util.List;

@Data
public class NewsApiResponseDto {

    private List<News> results;

    private String status;

    private String requestId;

    private int count;

    private String nextUrl;

}
