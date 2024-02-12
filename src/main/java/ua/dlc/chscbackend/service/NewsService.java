package ua.dlc.chscbackend.service;



import ua.dlc.chscbackend.model.News;
import ua.dlc.chscbackend.model.Ticker;

import java.time.LocalDateTime;
import java.util.List;

public interface NewsService {
    List<News> getAllNews(Ticker ticker, LocalDateTime firstDate, LocalDateTime lastDate);
}
