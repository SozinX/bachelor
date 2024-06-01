package com.sozinx.performance.service;

import com.sozinx.performance.entity.News;
import com.sozinx.performance.repository.NewsJpaRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PerformanceTestImpl implements PerformanceTest {
    @jakarta.annotation.Resource
    private NewsJpaRepository newsJpaRepository;
    @jakarta.annotation.Resource
    private JPADataService jpaDataService;
    @jakarta.annotation.Resource
    private SolrService solrService;
    @jakarta.annotation.Resource
    private ElasticService elasticService;

    private final ResourceLoader resourceLoader;

    public PerformanceTestImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<String> readFile(String filePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:static/" + filePath);
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
            String fileContent = new String(bytes, StandardCharsets.UTF_8);
            inputStream.close();
            return List.of(fileContent.split(",\n"));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public String startJpaTest(List<String> queries) {
        String jpaDefaultPerformance = "src/main/resources/static/results/jpa/jpa_performance.txt";
        String jpaDefaultResult = "src/main/resources/static/results/jpa/jpa_result.txt";
        doTest(queries, jpaDefaultPerformance, jpaDefaultResult, "jpa");
        return "OK";
    }
    public String startElasticTest(List<String> queries) {
        String elasticDefaultPerformance = "src/main/resources/static/results/elastic/elastic_performance.txt";
        String elasticDefaultResult = "src/main/resources/static/results/elastic/elastic_result.txt";
        doTest(queries, elasticDefaultPerformance, elasticDefaultResult, "elastic");
        return "OK";
    }
    public String startSolrTest(List<String> queries) {
        String solrDefaultPerformance = "src/main/resources/static/results/solr/solr_performance.txt";
        String solrDefaultResult = "src/main/resources/static/results/solr/solr_result.txt";
        doTest(queries, solrDefaultPerformance, solrDefaultResult, "solr");
        return "OK";
    }

    private void doTest(List<String> queries, String performanceFilePath, String resultFilePath, String service) {
        AtomicInteger solr = new AtomicInteger(1);
        AtomicInteger jpa = new AtomicInteger(1);
        AtomicInteger elastic = new AtomicInteger(1);
        try (PrintWriter performanceWriter = new PrintWriter(new FileWriter(performanceFilePath, true));
             PrintWriter resultWriter = new PrintWriter(new FileWriter(resultFilePath, true))) {
            queries.forEach(query -> {
                long break0 = System.currentTimeMillis();
                if(Objects.equals(service, "solr")) {
                    resultWriter.println(solrService.searchByString(query).size());
                    System.out.println("Solr: " + solr + "/" + queries.size());
                    solr.getAndIncrement();
                } else if (Objects.equals(service, "jpa")) {
                    resultWriter.println(jpaDataService.searchByString(query).size());
                    System.out.println("JPA: " + jpa + "/" + queries.size());
                    jpa.getAndIncrement();
                } else {
                    resultWriter.println(elasticService.searchByString(query).size());
                    System.out.println("Elastic: " + elastic + "/" + queries.size());
                    elastic.getAndIncrement();
                }
                performanceWriter.println(System.currentTimeMillis() - break0);
            });
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public void startValidationTest(List<String> queries) {
        String solrDefaultValidation = "src/main/resources/static/results/solr/solr_validation.txt";
        validationTest(queries, solrDefaultValidation, "solr");
        String jpaDefaultValidation = "src/main/resources/static/results/jpa/jpa_validation.txt";
        validationTest(queries, jpaDefaultValidation, "jpa");
        String elasticDefaultValidation = "src/main/resources/static/results/elastic/elastic_validation.txt";
        validationTest(queries, elasticDefaultValidation, "elastic");
    }
    private void validationTest(List<String> queries, String filePath, String service) {
        try (PrintWriter validationWriter = new PrintWriter(new FileWriter(filePath, true));) {
            queries.forEach(query -> {
                if(Objects.equals(service, "solr")) {
                    validationWriter.println(newsJpaRepository.findById(
                            Long.valueOf(solrService.searchByString(query).getFirst()))
                            .get().getName());
                } else if(Objects.equals(service, "jpa")) {
                    List<News> news = jpaDataService.searchByString(query);
                    if(!news.isEmpty()) {
                        validationWriter.println(news.getFirst().getName());
                    }
                    else {
                        validationWriter.println("No result found");
                    }
                } else {
                    validationWriter.println(elasticService.searchByString(query).getFirst().getName());

                }
            });
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
