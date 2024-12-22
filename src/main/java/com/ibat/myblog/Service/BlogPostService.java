package com.ibat.myblog.Service;

import com.ibat.myblog.Model.*;
import com.ibat.myblog.Repository.*;
import com.ibat.myblog.Util.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Page<BlogPost> blogPosts = blogPostRepository.findByStatus("published", pageable);
        
        return blogPosts.map(blogPost -> {
            if (blogPost.getPreview() == null) {
                blogPost.generatePreview();
            }
            return BlogPostListDTO.fromBlogPost(blogPost);
        });
    }

    public BlogPost createOrUpdatePost(BlogPost blogPost) {
        blogPost.generatePreview(); // 生成预览内容
        // 保存博客文章
        BlogPost savedPost = blogPostRepository.save(blogPost);

// 提取并处理链接
List<String> urls = extractUrls(blogPost.getContent());
for (String url : urls) {
    SharedLink linkInfo = LinkExtractor.extractLinkInfo(url);
    if (linkInfo != null) {
        linkInfo.setBlogPostId(savedPost.getPostId());
        linkInfo.setUserId(savedPost.getUserId());
        linkInfo.setCreatedAt(LocalDateTime.now());
        linkInfo.setUpdatedAt(LocalDateTime.now());
        sharedLinkRepository.save(linkInfo);
    }
}

        // 分析博客内容中的媒体内容
        try {
            ResponseEntity<String> aiResponse = aiRequest.makeRequest(blogPost.getContent());
            if (aiResponse.getStatusCode() == HttpStatus.OK && aiResponse.getBody() != null) {
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

    private List<Media> parseAIResponse(String aiResponse) throws IOException {
        List<Media> mediaList = new ArrayList<>();
        String[] entries = aiResponse.split("\n");
    
        for (String entry : entries) {
            String[] parts = entry.split("\\|");
            if (parts.length >= 3) {
                Media media = new Media();
                media.setType(parts[0].trim());
                media.setTitle(parts[1].trim());
                media.setArtist(parts[2].trim());
                media.setPublishDate(LocalDateTime.now());
                
                // 获取封面
                String coverUrl = null;
                if ("音乐".equals(media.getType())) {
                    MusicCoverFetcher musicFetcher = new MusicCoverFetcher();
                    coverUrl = musicFetcher.getCoverUrl(media.getTitle(), media.getArtist());
                } else if ("影视".equals(media.getType())) {
                    TMDBAPI tmdbApi = new TMDBAPI();
                    coverUrl = tmdbApi.getMoviePoster(media.getTitle(), "w500");
                }
                media.setPicture(coverUrl);
                mediaList.add(media);
            }
        }
        return mediaList;
    }
    private List<String> extractUrls(String content) {
        List<String> urls = new ArrayList<>();
        String urlRegex = "https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        return urls;
    }
}