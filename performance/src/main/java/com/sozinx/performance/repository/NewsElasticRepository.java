package com.sozinx.performance.repository;

import com.sozinx.performance.entity.ENews;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface NewsElasticRepository extends ElasticsearchRepository<ENews, Long> {
    @Query("{\"bool\": {\"should\": [{\"match\": {\"name\": \"?0\"}},{\"match\": {\"content\": \"?0\"}}]}}")
    List<ENews> getENewsByNameContainingOrContentContaining(String name, Pageable pageable);
}
