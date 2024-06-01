package com.embedding.embedding.repository;

import com.embedding.embedding.entity.News;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsJpaRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n")
    List<News> getAll(Pageable pageable);
}
