package com.embedding.embedding.service;

import com.embedding.embedding.entity.News;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService{

    private static final float MATCH_THRESHOLD = 0.7f;
    private static final int MATCH_CNT = 1;

    private final JdbcClient jdbcClient;

    private final EmbeddingClient embeddingClient;

    @Autowired
    public SearchServiceImpl(JdbcClient jdbcClient, EmbeddingClient embeddingClient) {
        this.jdbcClient = jdbcClient;
        this.embeddingClient = embeddingClient;
    }

    public List<News> searchByString(String value) {
        List<Double> promptEmbedding = embeddingClient.embed(value);

        JdbcClient.StatementSpec query = jdbcClient.sql(
                        "SELECT id, name, content " +
                                "FROM news " +
                                "WHERE 1 - (content_embedding <=> :value::vector) > :match_threshold " +
                                "OR (name_embedding <=> :value::vector) > :match_threshold " +
                                "ORDER BY content_embedding <=> :value::vector LIMIT :match_cnt")
                .param("value", promptEmbedding.toString())
                .param("match_threshold", MATCH_THRESHOLD)
                .param("match_cnt", MATCH_CNT);
        return query.query(News.class).list();
    }
}
