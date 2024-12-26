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
    private String status; // 1表示已发布，2表示草稿，3表示已删除

    private Integer viewCount;

    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(length = 500)
    private String preview;  // 文章预览内容，最多500字符
    
    // 添加生成预览内容的方法
    public void generatePreview() {
        if (this.content != null) {
            this.preview = this.content.length() > 500 ? 
                this.content.substring(0, 500) : this.content;
        }
    }
}