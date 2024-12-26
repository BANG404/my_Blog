package com.ibat.myblog.Controller;

import com.ibat.myblog.Model.BlogPost;
import com.ibat.myblog.Service.BlogPostService;
import com.ibat.myblog.Service.UserService;
import com.ibat.myblog.Model.BlogPostListDTO;
import com.ibat.myblog.Model.PBlogPostDTO;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Tag(name = "博客文章管理", description = "博客文章的CRUD接口")
@RestController
@RequestMapping("/api/blog")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;
    
    @Autowired
    private UserService userService;

    @Operation(summary = "创建博客文章", description = "创建一篇新的博客文章")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping("/create")
    public ResponseEntity<BlogPost> createPost(
            @Parameter(description = "博客文章内容", required = true) @RequestBody PBlogPostDTO blogPostDTO) {
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(blogPostDTO.getTitle());
        blogPost.setContent(blogPostDTO.getContent());
        
        // 获取当前登录用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 根据用户名获取用户ID
        Integer userId = userService.findUserIdByUsername(username);
        if (userId == null) {
            throw new RuntimeException("用户未找到");
        }
        blogPost.setUserId(userId);
        
        blogPost.setStatus("1"); // 1表示已发布，2表示草稿，3表示已删除

        // 设置创建时间
        blogPost.setPublishedAt(LocalDateTime.now());
        blogPost.setCreatedAt(LocalDateTime.now());
        // 设置更新时间
        blogPost.setUpdatedAt(LocalDateTime.now());
        
        BlogPost createdPost = blogPostService.createOrUpdatePost(blogPost);
        return ResponseEntity.ok(createdPost);
    }

    @Operation(summary = "更新博客文章", description = "更新已有的博客文章")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PutMapping("/{postId}")
    public ResponseEntity<BlogPost> updatePost(
            @Parameter(description = "文章ID", required = true) @PathVariable Integer postId,
            @Parameter(description = "更新的文章内容", required = true) @RequestBody BlogPost blogPost) {
        blogPost.setPostId(postId);
        BlogPost updatedPost = blogPostService.editPost(blogPost);
        return ResponseEntity.ok(updatedPost);
    }

    @Operation(summary = "删除博客文章")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "要删除的文章ID", required = true) @PathVariable Integer postId) {
        blogPostService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取已发布的博客文章列表", description = "分页获取已发布的文章")
    @GetMapping("/posts")
    public ResponseEntity<Page<BlogPostListDTO>> getPublishedPosts(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        Page<BlogPostListDTO> posts = blogPostService.getPublishedPosts(page, size);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "获取单篇博客文章", description = "根据文章ID获取博客文章详情")
    @ApiResponse(responseCode = "200", description = "获取成功")
    @GetMapping("/{postId}")
    public ResponseEntity<BlogPost> getPost(
            @Parameter(description = "文章ID", required = true) 
            @PathVariable Integer postId) {
        BlogPost post = blogPostService.getPost(postId);
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "搜索博客文章", description = "根据关键词搜索文章标题和内容")
    @GetMapping("/search")
    public ResponseEntity<Page<BlogPost>> searchPosts(
            @Parameter(description = "搜索关键词") 
            @RequestParam(required = false) String keyword,
            @Parameter(description = "页码") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") 
            @RequestParam(defaultValue = "10") int size) {
            
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BlogPost> posts = blogPostService.searchBlogPosts(keyword, pageRequest);
        return ResponseEntity.ok(posts);
    }
    
}