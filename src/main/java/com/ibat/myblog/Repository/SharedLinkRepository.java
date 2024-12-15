package com.ibat.myblog.Repository;

import com.ibat.myblog.Model.SharedLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharedLinkRepository extends JpaRepository<SharedLink, Integer> {
    // 可以添加自定义查询方法，如按用户ID或博客文章ID查找分享链接
    List<SharedLink> findByUserId(Integer userId);
    List<SharedLink> findByBlogPostId(Integer blogPostId);
}
