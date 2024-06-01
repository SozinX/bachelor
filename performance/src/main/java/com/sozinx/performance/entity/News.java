package com.sozinx.performance.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String name;

    private String content;
}
