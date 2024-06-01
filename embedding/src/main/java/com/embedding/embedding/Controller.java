package com.embedding.embedding;


import com.embedding.embedding.service.PerformanceTest;
import com.embedding.embedding.service.TextToVectorService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class Controller {
    @Resource
    private TextToVectorService textToVectorService;
    @Resource
    private PerformanceTest performanceTest;

    @GetMapping("/text_to_vector")
    public String embedEntitiesValues() {
//        textToVectorService.textToVectorForAllNews();
        return "OK";
    }

    @GetMapping("/start_test_performance")
    public String startPerformanceTest() {
        List<String> queries = performanceTest.readFile("queries.txt");
        performanceTest.startEmbeddingTest(queries);
        return "OK";
    }

    @GetMapping("/start_validation_test")
    public String validationTest() {
        List<String> queries = performanceTest.readFile("queries.txt");
        performanceTest.validationTest(queries);
        return "OK";
    }
}
