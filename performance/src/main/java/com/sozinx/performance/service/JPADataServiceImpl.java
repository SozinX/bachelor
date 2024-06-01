package com.sozinx.performance.service;

import com.sozinx.performance.entity.News;
import com.sozinx.performance.repository.NewsJpaRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sozinx.performance.utils.Constants.MAX_RESULTS_IN_SEARCH;

@Service
public class JPADataServiceImpl implements JPADataService {
    @Resource
    private NewsJpaRepository newsJpaRepository;

    @Override
    public List<News> searchByString(String value) {
        Pageable pageable = PageRequest.of(0, MAX_RESULTS_IN_SEARCH);
        return newsJpaRepository.getNewsByNameContainingOrContentContaining(value, value, pageable);
    }
}
