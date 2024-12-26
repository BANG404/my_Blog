package com.ibat.myblog.Service;

import com.ibat.myblog.Model.BlogPost;
import com.ibat.myblog.Repository.BlogPostRepository;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service 
@Slf4j
public class RssService {
    
    @Value("${rss.page-size:10}")
    private int pageSize;

    @Autowired
    private BlogPostRepository blogPostRepository;

    public String generateRssFeed() {
        try {
            SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType("rss_2.0");
            feed.setTitle("我的博客");
            feed.setDescription("最新博客文章订阅");
            feed.setLink("localhost:3000"); // 替换为你的域名
            feed.setLanguage("zh-cn");

            // 获取最新的文章,使用配置的页面大小
            Page<BlogPost> posts = blogPostRepository.findByStatus(
                "1",
                PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "publishedAt"))
            );

            List<SyndEntry> entries = new ArrayList<>();
            for (BlogPost post : posts.getContent()) {
                SyndEntry entry = new SyndEntryImpl();
                entry.setTitle(post.getTitle());
                entry.setLink("localhost:3000/post/" + post.getPostId());
                
                // 设置完整的文章内容
                SyndContent content = new SyndContentImpl();
                content.setType("text/html");
                content.setValue(formatContent(post.getContent()));
                List<SyndContent> contents = new ArrayList<>();
                contents.add(content);
                entry.setContents(contents);
                
                // 设置预览作为描述
                SyndContent description = new SyndContentImpl();
                description.setType("text/plain");
                description.setValue(post.getPreview());
                entry.setDescription(description);
                
                // 设置作者
                // entry.setAuthor(post.getAuthor() != null ? post.getAuthor() : "匿名");
                
                // 设置发布时间
                entry.setPublishedDate(Date.from(post.getPublishedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()));
                
                entries.add(entry);
            }
            
            feed.setEntries(entries);
            
            return new SyndFeedOutput().outputString(feed);
        } catch (Exception e) {
            log.error("生成RSS feed失败", e);
            throw new RuntimeException("生成RSS feed失败", e);
        }
    }

    private String formatContent(String content) {
        if (content == null) return "";
        // 格式化文章内容,添加基本样式
        return String.format("""
            <div class="blog-content">
                %s
            </div>
            """, content);
    }
}
