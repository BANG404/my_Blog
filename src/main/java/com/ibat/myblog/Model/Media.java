package com.ibat.myblog.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mediaId;

    private Integer userId;
    private Integer blogPostId;

    @Column(length = 200)
    private String title;

    @Column(length = 255)
    private String picture;

    @Column(length = 20)
    private String type;  //'影视', '音乐' 枚举 

    
    @Column(length = 2083)
    private String url;

    @Column(length = 100)
    private String artist;

    private LocalDateTime publishDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
