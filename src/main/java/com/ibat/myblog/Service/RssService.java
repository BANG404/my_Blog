package com.ibat.myblog.Service;

import com.ibat.myblog.Model.BlogPost;
import com.ibat.myblog.Repository.BlogPostRepository;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RssService {
    
    @Autowired
    private BlogPostRepository blogPostRepository;

    public String generateRssFeed() {
        try {
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("rss_2.0");
            feed.setTitle("我的博客");
            feed.setDescription("最新博客文章订阅");
            feed.setLink("http://yourdomain.com"); // 替换为你的域名
            feed.setLanguage("zh-cn");

            // 获取最新的10篇已发布文章
            Page<BlogPost> posts = blogPostRepository.findByStatus(
                "published",
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "publishedAt"))
            );

            List<SyndEntry> entries = new ArrayList<>();
            for (BlogPost post : posts.getContent()) {
                SyndEntry entry = new SyndEntryImpl();
                entry.setTitle(post.getTitle());
                entry.setLink("http://yourdomain.com/posts/" + post.getPostId()); // 替换为你的文章URL格式
                
                // 设置内容
                SyndContent description = new SyndContentImpl();
                description.setType("text/html");
                description.setValue(post.getPreview());
                entry.setDescription(description);
                
                // 设置发布时间
                entry.setPublishedDate(Date.from(post.getPublishedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()));
                
                entries.add(entry);
            }
            
            feed.setEntries(entries);
            
            return new SyndFeedOutput().outputString(feed);
        } catch (Exception e) {
            throw new RuntimeException("生成RSS feed失败", e);
        }
    }
}
