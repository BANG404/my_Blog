package com.ibat.myblog.Model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BlogPostListDTO {
    private Integer postId;
    private String title;
    private String preview;
    private String status;  // 1表示已发布，2表示草稿，3表示已删除
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
