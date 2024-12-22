package com.ibat.myblog.Model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BlogPostListDTO {
    private Integer postId;
    private String title;
    private String preview;
    private String status;
    private Integer viewCount;
    private LocalDateTime publishedAt;
    
    public static BlogPostListDTO fromBlogPost(BlogPost blogPost) {
        BlogPostListDTO dto = new BlogPostListDTO();
        dto.setPostId(blogPost.getPostId());
        dto.setTitle(blogPost.getTitle());
        dto.setPreview(blogPost.getPreview());
        dto.setStatus(blogPost.getStatus());
        dto.setViewCount(blogPost.getViewCount());
        dto.setPublishedAt(blogPost.getPublishedAt());
        return dto;
    }
}
