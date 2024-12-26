package com.ibat.myblog.Controller;

import com.ibat.myblog.Service.RssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class RssController {
    
    @Autowired
    private RssService rssService;

    @Value("${rss.cache-duration:3600}")
    private int cacheDuration;

    @GetMapping(value = "/rss", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getRssFeed() {
        try {
            String rssFeed = rssService.generateRssFeed();
            return ResponseEntity.ok()
                .header("Content-Type", "application/xml; charset=utf-8")
                .header("Cache-Control", "max-age=" + cacheDuration)
                .body(rssFeed);
        } catch (Exception e) {
            log.error("获取RSS订阅失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("<?xml version=\"1.0\" encoding=\"UTF-8\"?><error>获取订阅内容失败</error>");
        }
    }
}
