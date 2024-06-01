package com.embedding.embedding.service;

import java.util.List;

public interface PerformanceTest {
    List<String> readFile(String filepath);
    String startEmbeddingTest(List<String> queries);
    void validationTest(List<String> queries);
}
