package com.sozinx.performance.service;

import com.sozinx.performance.entity.ENews;

import java.util.List;

public interface ElasticService {
    void indexDataFromDataBase();

    List<ENews> searchByString(String value);
}
