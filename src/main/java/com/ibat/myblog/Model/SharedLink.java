package com.ibat.myblog.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shared_links")
public class SharedLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer linkId;

    private Integer userId;
    private Integer blogPostId;

    @Column(length = 255)
    private String icon;

    @Column(length = 200)
    private String title;

    @Column(length = 2083)
    private String url;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
