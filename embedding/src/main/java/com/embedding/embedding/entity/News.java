package com.embedding.embedding.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Table(name="news")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @Basic
    @Type(JsonType.class)
    @Column(name = "name_embedding", columnDefinition = "vector")
    private List<Double> nameEmbedding;

    @Basic
    @Type(JsonType.class)
    @Column(name = "content_embedding", columnDefinition = "vector")
    private List<Double> contentEmbedding;
}
