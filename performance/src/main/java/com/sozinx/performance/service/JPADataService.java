package com.sozinx.performance.service;

import com.sozinx.performance.entity.News;

import java.util.List;

public interface JPADataService {
    List<News> searchByString(String value);
}
