package com.embedding.embedding.service;

import com.embedding.embedding.entity.News;

import java.util.List;

public interface SearchService {
    List<News> searchByString(String value);
}
