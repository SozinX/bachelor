package com.sozinx.performance;


import com.sozinx.performance.service.ElasticService;
import com.sozinx.performance.service.PerformanceTest;
import com.sozinx.performance.service.SolrService;
import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class Controller {
    @Resource
    private SolrService solrService;
    @Resource
    private ElasticService elasticService;
    @Resource
    private PerformanceTest performanceTest;


    @GetMapping("/index_solr")
    public String indexDataInSolr() {
        solrService.indexDataFromDataBase();
        return "OK";
    }

    @GetMapping("/index_elastic")
    public String indexDataInElastic() {
        elasticService.indexDataFromDataBase();
        return "OK";
    }

    @GetMapping("/start_test_performance")
    public String startPerformanceTest() {
        List<String> queries = performanceTest.readFile("queries.txt");
//        performanceTest.startJpaTest(queries);
//        performanceTest.startSolrTest(queries);
        performanceTest.startElasticTest(queries);
        return "OK";
    }

    @GetMapping("/start_validation_test")
    public String startValidationTest() {
        List<String> queries = performanceTest.readFile("queries.txt");
        performanceTest.startValidationTest(queries);
        return "OK";
    }
}
