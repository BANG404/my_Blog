package com.ibat.myblog.Controller;

import com.ibat.myblog.Service.RssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RssController {
    
    @Autowired
    private RssService rssService;

    @GetMapping(value = "/rss", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getRssFeed() {
        String rssFeed = rssService.generateRssFeed();
        return ResponseEntity.ok()
            .header("Content-Type", "application/xml; charset=utf-8")
            .body(rssFeed);
    }
}
