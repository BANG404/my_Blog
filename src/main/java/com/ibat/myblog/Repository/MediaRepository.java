package com.ibat.myblog.Repository;

import com.ibat.myblog.Model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {
    // 可以添加自定义查询方法，如按博客文章ID查找媒体
    List<Media> findByBlogPostId(Integer blogPostId);
//    查找用户最新分享的音乐
    List<Media> findTop1MusicByUserIdOrderByPublishDateDesc(Integer userId);
//    查找用户最新分享的影视
    List<Media> findTop1MovieByUserIdOrderByPublishDateDesc(Integer userId);
}