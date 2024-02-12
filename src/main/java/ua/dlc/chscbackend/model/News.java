package ua.dlc.chscbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class News {

    private String id;
    private Publisher publisher;
    private String title;
    private String author;

    @JsonProperty("published_utc")
    private String publishedUtc;

    @JsonProperty("article_url")
    private String articleUrl;
    private List<String> tickers;

    @JsonProperty("image_url")
    private String imageUrl;
    private String description;
    private List<String> keywords;
}
