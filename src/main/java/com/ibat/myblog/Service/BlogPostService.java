package com.ibat.myblog.Service;

import com.ibat.myblog.Model.*;
import com.ibat.myblog.Repository.*;
import com.ibat.myblog.Utils.*;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class BlogPostService {
    @Autowired
    private BlogPostRepository blogPostRepository;
    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private AIRequest aiRequest;
    @Autowired
    private SharedLinkRepository sharedLinkRepository;

    public Page<BlogPostListDTO> getPublishedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        Page<BlogPost> blogPosts = blogPostRepository.findByStatus("1", pageable);

        return blogPosts.map(blogPost -> {
            if (blogPost.getPreview() == null) {
                blogPost.generatePreview();
            }
            return BlogPostListDTO.fromBlogPost(blogPost);
        });
    }

    public BlogPost createOrUpdatePost(BlogPost blogPost) {
        if (blogPost.getPreview() == null) {
            blogPost.generatePreview();
        }
        BlogPost savedPost = blogPostRepository.save(blogPost);

        // 提取并保存链接
        List<SharedLink> links = UrlMatcher.extractLinks(
            blogPost.getContent(), 
            savedPost.getUserId(), 
            savedPost.getPostId()
        );
        for (SharedLink link : links) {
            sharedLinkRepository.save(link);
        }

        // 分析博客内容中的媒体内容
        try {
            ResponseEntity<String> aiResponse = aiRequest.makeRequest(blogPost.getContent());
            if (aiResponse.getStatusCode() == HttpStatus.OK && aiResponse.getBody() != null) {
                System.out.println(aiResponse.getBody());
                List<Media> mediaList = parseAIResponse(aiResponse.getBody());
                for (Media media : mediaList) {
                    media.setUserId(blogPost.getUserId());
                    media.setBlogPostId(savedPost.getPostId());
                    media.setCreatedAt(LocalDateTime.now());
                    media.setUpdatedAt(LocalDateTime.now());
                    mediaRepository.save(media);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return savedPost;
    }

    public BlogPost editPost(BlogPost blogPost) {
        BlogPost existingPost = blogPostRepository.findById(blogPost.getPostId())
                .orElseThrow(() -> new RuntimeException("博文不存在"));

        existingPost.setTitle(blogPost.getTitle());
        existingPost.setContent(blogPost.getContent());
        existingPost.setStatus(blogPost.getStatus());
        existingPost.setUpdatedAt(LocalDateTime.now());
        existingPost.generatePreview(); // 更新时重新生成预览内容

        BlogPost updatedPost = createOrUpdatePost(existingPost);
        return updatedPost;
    }

    @Transactional  // 为删除方法单独添加事务
    public void deletePost(Integer postId) {
        BlogPost existingPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("博文不存在"));

        // 删除相关的媒体信息
        mediaRepository.deleteByBlogPostId(postId);

        // 删除相关的分享链接
        sharedLinkRepository.deleteByBlogPostId(postId);

        // 删除博文
        blogPostRepository.delete(existingPost);
    }

    public BlogPost getPost(Integer postId) {
        return blogPostRepository.findById(postId)
            .orElseThrow(() -> new RuntimeException("文章不存在"));
    }

    public Page<BlogPost> searchBlogPosts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return blogPostRepository.findByStatus("1", pageable);
        }
        return blogPostRepository.searchByKeyword(keyword.trim(), pageable);
    }

    private List<Media> parseAIResponse(String aiResponse) throws IOException {
        List<Media> mediaList = new ArrayList<>();
        if (aiResponse == null || aiResponse.trim().isEmpty()) {
            return mediaList;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(aiResponse);
        
        // 获取content值
        String content = rootNode.path("choices")
                               .path(0)
                               .path("message")
                               .path("content")
                               .asText();

        if (content == null || content.trim().isEmpty()) {
            return mediaList;
        }

        String[] entries = content.trim().split("\n");
        for (String entry : entries) {
            try {
                String[] parts = entry.trim().split("\\|");
                if (parts.length < 3) {
                    continue;  // 跳过格式不正确的条目
                }

                String type = parts[0].trim();
                String title = parts[1].trim();
                String artist = parts[2].trim();

                

                // 验证标题是否为空
                if (title.isEmpty()) {
                    continue;
                }

                Media media = new Media();
                media.setType(type);
                media.setTitle(title);
                media.setArtist("未知".equals(artist) ? null : artist);
                media.setPublishDate(LocalDateTime.now());

                // 获取封面URL（保持原有逻辑）
                String coverUrl = null;
                try {
                    switch (type) {
                        case "音乐":
                            MusicCoverFetcher musicFetcher = new MusicCoverFetcher();
                            coverUrl = musicFetcher.getCoverUrl(title, artist);
                            break;
                        case "影视":
                            TMDBAPI tmdbApi = new TMDBAPI();
                            coverUrl = tmdbApi.getMoviePoster(title, "w500");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                media.setPicture(coverUrl);
                mediaList.add(media);
            } catch (Exception e) {
                // 记录错误但继续处理下一个条目
                e.printStackTrace();
            }
        }
        return mediaList;
    }
}