package com.embedding.embedding.service;


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
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PerformanceTestImpl implements PerformanceTest {
    @jakarta.annotation.Resource
    private SearchService searchService;

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

    public String startEmbeddingTest(List<String> queries) {
        String embeddingDefaultPerformance = "src/main/resources/static/results/embedding/embedding_performance.txt";
        String embeddingDefaultResult = "src/main/resources/static/results/embedding/embedding_result.txt";
        doTest(queries, embeddingDefaultPerformance, embeddingDefaultResult);
        return "OK";
    }

    private void doTest(List<String> queries, String performanceFilePath, String resultFilePath) {
        AtomicInteger embedding = new AtomicInteger(1);
        try (PrintWriter performanceWriter = new PrintWriter(new FileWriter(performanceFilePath, true));
             PrintWriter resultWriter = new PrintWriter(new FileWriter(resultFilePath, true))) {
            queries.forEach(query -> {
                long break0 = System.currentTimeMillis();
                resultWriter.println(searchService.searchByString(query).size());
                System.out.println("Embedding: " + embedding + "/" + queries.size());
                embedding.getAndIncrement();
                performanceWriter.println(System.currentTimeMillis() - break0);
            });
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public void validationTest(List<String> queries) {
        String embeddingDefaultValidation = "src/main/resources/static/results/embedding/embedding_validation.txt";
        try (PrintWriter validationWriter = new PrintWriter(new FileWriter(embeddingDefaultValidation, true));) {
            queries.forEach(query -> {
                validationWriter.println(searchService.searchByString(query).getFirst().getName());
            });
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
