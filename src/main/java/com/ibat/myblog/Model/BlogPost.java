package com.ibat.myblog.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blog_posts")
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(length = 20)
    private String status;

    private Integer viewCount;

    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}