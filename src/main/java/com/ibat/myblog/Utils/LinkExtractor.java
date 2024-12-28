package com.ibat.myblog.Utils;

import com.ibat.myblog.Model.SharedLink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

public class LinkExtractor {
    public static SharedLink extractLinkInfo(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            SharedLink link = new SharedLink();
            
            // 设置URL
            link.setUrl(url);
            
            // 提取标题
            String title = doc.title();
            link.setTitle(title);
            
            // 提取网站图标
            String iconUrl = doc.select("link[rel~=icon]").attr("href");
            if (iconUrl.startsWith("/")) {
                iconUrl = new URL(new URL(url), iconUrl).toString();
            }
            link.setIcon(iconUrl);
            
            return link;
        } catch (Exception e) {
            return null;
        }
    }
}