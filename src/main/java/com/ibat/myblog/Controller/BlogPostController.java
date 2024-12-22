package com.ibat.myblog.Controller;

import com.ibat.myblog.Model.BlogPost;
import com.ibat.myblog.Service.BlogPostService;
import com.ibat.myblog.Model.BlogPostListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/blog")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @PostMapping("/create")
    public BlogPost createPost(@RequestBody BlogPost blogPost) {
        return blogPostService.createOrUpdatePost(blogPost);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<BlogPost> updatePost(@PathVariable Integer postId, @RequestBody BlogPost blogPost) {
        blogPost.setPostId(postId);
        BlogPost updatedPost = blogPostService.editPost(blogPost);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        blogPostService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<BlogPostListDTO>> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BlogPostListDTO> posts = blogPostService.getPublishedPosts(page, size);
        return ResponseEntity.ok(posts);
    }
}