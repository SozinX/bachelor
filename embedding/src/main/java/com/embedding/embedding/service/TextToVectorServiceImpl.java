package com.embedding.embedding.service;

import com.embedding.embedding.entity.News;
import com.embedding.embedding.repository.NewsJpaRepository;
import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextToVectorServiceImpl implements TextToVectorService{

    @Resource
    private NewsJpaRepository newsJpaRepository;
    private final EmbeddingClient embeddingClient;

    @Autowired
    public TextToVectorServiceImpl(EmbeddingClient embeddingClient) {
        this.embeddingClient = embeddingClient;
    }

    @Override
    public void textToVectorForAllNews() {
        for(int i = 3; i < 100; i++) {
            List<News> entities = newsJpaRepository.getAll(PageRequest.of(i, 1000));
            for (News entity : entities) {
                if(entity.getContentEmbedding() != null || entity.getNameEmbedding() != null || entity.getContent().length() > 8191) {
                    continue;
                }
                List<Double> contentEmbedding = embeddingClient.embed(entity.getContent());
                List<Double> nameEmbedding = embeddingClient.embed(entity.getContent());
                entity.setContentEmbedding(contentEmbedding);
                entity.setNameEmbedding(nameEmbedding);
                newsJpaRepository.save(entity);
            }
        }
    }
}
