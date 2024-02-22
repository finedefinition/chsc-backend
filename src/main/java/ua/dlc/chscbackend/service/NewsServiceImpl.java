package ua.dlc.chscbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.dlc.chscbackend.constants.ApplicationConstants;
import ua.dlc.chscbackend.dto.response.NewsApiResponseDto;
import ua.dlc.chscbackend.model.News;
import ua.dlc.chscbackend.model.Ticker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

    @Service
    public class NewsServiceImpl implements NewsService {
        @Value("${polygon.api.key}")
        private String apiKey;


        private final RestTemplate restTemplate = new RestTemplate();

        @Override
        public List<News> getAllNews(Ticker ticker, LocalDateTime firstDate, LocalDateTime lastDate) {
            String startDate = firstDate.format(DateTimeFormatter.ISO_DATE_TIME);
            String endDate = lastDate.format(DateTimeFormatter.ISO_DATE_TIME);
            String selectedTicker = ticker.name();
            String companyName = ticker.getFullName(); // Get the full name of the ticker
            String url = String.format(ApplicationConstants.API_URL, selectedTicker, apiKey, startDate, endDate);
            ResponseEntity<NewsApiResponseDto> response = restTemplate.exchange(url, HttpMethod.GET, null, NewsApiResponseDto.class);
            NewsApiResponseDto newsApiResponseDto = response.getBody();

            if (newsApiResponseDto != null && newsApiResponseDto.getResults() != null) {
                List<News> newsItems = newsApiResponseDto.getResults().stream()
                        .filter(news -> news.getTitle().contains(companyName)) // Filter by ticker's full name
                        .collect(Collectors.toList());
                return newsItems;
            }

            return new ArrayList<>();
        }
    }


