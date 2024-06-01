package com.sozinx.performance.service;

import java.util.List;

public interface PerformanceTest {
    List<String> readFile(String filepath);
    String startJpaTest(List<String> queries);
    String startElasticTest(List<String> queries);
    String startSolrTest(List<String> queries);
    void startValidationTest(List<String> queries);
}
