package com.sozinx.performance.service;

import com.sozinx.performance.entity.ENews;
import com.sozinx.performance.entity.News;
import com.sozinx.performance.repository.NewsElasticRepository;
import com.sozinx.performance.repository.NewsJpaRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sozinx.performance.utils.Constants.MAX_RESULTS_IN_SEARCH;

@Service
public class ElasticServiceImpl implements ElasticService{
    @Resource
    private NewsJpaRepository newsJpaRepository;
    @Resource
    private NewsElasticRepository newsElasticRepository;
    @Override
    public void indexDataFromDataBase() {
        for(int i = 0; i < 520; i++) {
            List<News> newses = newsJpaRepository.getAll(PageRequest.of(i, 1000));
            for(News news: newses) {
                newsElasticRepository.save(new ENews(news.getId(), news.getName(), news.getContent()));
            }
            System.out.println("Added successfully: " + i);
        }
    }

    @Override
    public List<ENews> searchByString(String value) {
        Pageable pageable = PageRequest.of(0, MAX_RESULTS_IN_SEARCH);
        return newsElasticRepository.getENewsByNameContainingOrContentContaining(value, pageable);
    }
}
