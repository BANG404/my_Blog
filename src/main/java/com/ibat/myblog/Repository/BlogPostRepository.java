package com.ibat.myblog.Repository;

import com.ibat.myblog.Model.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    // 可以添加自定义查询方法，如按用户ID查找博客文章
    List<BlogPost> findByUserId(Integer userId);
    Page<BlogPost> findByStatus(String status, Pageable pageable);

    @Query("SELECT b FROM BlogPost b WHERE " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
           "LOWER(b.content) LIKE LOWER(CONCAT('%', ?1, '%'))) AND " +
           "b.status = '1'")
    Page<BlogPost> searchByKeyword(String keyword, Pageable pageable);
}
