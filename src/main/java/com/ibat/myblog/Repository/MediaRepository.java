package com.ibat.myblog.Repository;

import com.ibat.myblog.Model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {
    // 可以添加自定义查询方法，如按博客文章ID查找媒体
    List<Media> findByBlogPostId(Integer blogPostId);

    // 根据用户ID和类型查找最新的一条记录
    Optional<Media> findFirstByUserIdAndTypeOrderByPublishDateDesc(Integer userId, String type);
    
    // 根据用户ID查找最新的音乐
    Optional<Media> findFirstByUserIdAndTypeEqualsOrderByPublishDateDesc(Integer userId, String type);
    
    // 根据博文ID删除所有相关媒体
    void deleteByBlogPostId(Integer blogPostId);
}