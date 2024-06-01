package com.sozinx.performance.entity;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Document(indexName = "news")
public class ENews {

    private Long id;

    private String name;

    private String content;
}
