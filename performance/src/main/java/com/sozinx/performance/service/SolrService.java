package com.sozinx.performance.service;

import java.util.List;

public interface SolrService {
    void indexDataFromDataBase();

    List<String> searchByString(String value);
}
