package com.ibat.myblog.Repository;

import com.ibat.myblog.Model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    // 可以添加自定义查询方法，如按用户ID查找博客文章
    List<BlogPost> findByUserId(Integer userId);
}
