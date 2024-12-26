package com.ibat.myblog.Util;

import com.ibat.myblog.Model.SharedLink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlMatcher {
    private static final Pattern URL_PATTERN = Pattern.compile(
            "(https?://[\\w-]+(\\.[\\w-]+)+[\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-]?)");

    public static List<SharedLink> extractLinks(String content, Integer userId, Integer postId) {
        List<SharedLink> links = new ArrayList<>();
        Set<String> uniqueUrls = new HashSet<>();
        
        Matcher matcher = URL_PATTERN.matcher(content);
        while (matcher.find()) {
            String url = matcher.group();
            if (uniqueUrls.add(url)) { // 去重
                SharedLink link = createSharedLink(url, userId, postId);
                if (link != null) {
                    links.add(link);
                }
            }
        }
        return links;
    }

    private static SharedLink createSharedLink(String url, Integer userId, Integer postId) {
        try {
            SharedLink link = new SharedLink();
            link.setUrl(url);
            link.setUserId(userId);
            link.setBlogPostId(postId);
            link.setCreatedAt(java.time.LocalDateTime.now());
            link.setUpdatedAt(java.time.LocalDateTime.now());

            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(5000)
                        .get();
                
                // 获取标题（先尝试常规标题，然后是 og:title，最后用 URL）
                String title = doc.title();
                if (title == null || title.trim().isEmpty()) {
                    title = doc.select("meta[property=og:title]").attr("content");
                    if (title.isEmpty()) {
                        title = url;
                    }
                }
                link.setTitle(title.trim());
                
                // 获取网站图标
                String iconUrl = findFavicon(doc, url);
                link.setIcon(iconUrl);
                
            } catch (Exception e) {
                link.setTitle(url);
                link.setIcon("");
            }

            return link;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String findFavicon(Document doc, String baseUrl) {
        try {
            String iconUrl = "";
            // 扩展图标选择器列表
            String[] selectors = {
                "link[rel='shortcut icon']",
                "link[rel='icon']",
                "link[rel='apple-touch-icon']",
                "link[rel='apple-touch-icon-precomposed']",
                "link[type='image/x-icon']",
                "link[type='image/png']",
                "link[type='image/jpeg']",
                "link[type='image/gif']",
                "meta[property='og:image']"
            };

            // 尝试所有选择器
            for (String selector : selectors) {
                iconUrl = doc.select(selector).attr("href");
                if (selector.contains("og:image")) {
                    iconUrl = doc.select(selector).attr("content");
                }
                if (!iconUrl.isEmpty()) break;
            }

            // 如果没有找到图标，尝试默认路径
            if (iconUrl.isEmpty()) {
                String[] defaultPaths = {"/favicon.ico", "/favicon.png", "/apple-touch-icon.png"};
                for (String path : defaultPaths) {
                    try {
                        java.net.URL iconURL = new java.net.URL(new java.net.URL(baseUrl), path);
                        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) iconURL.openConnection();
                        connection.setRequestMethod("HEAD");
                        connection.setConnectTimeout(3000);
                        if (connection.getResponseCode() == 200) {
                            iconUrl = iconURL.toString();
                            break;
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }

            // 处理相对路径
            if (!iconUrl.isEmpty()) {
                if (iconUrl.startsWith("//")) {
                    return "https:" + iconUrl;
                } else if (iconUrl.startsWith("/") || !iconUrl.startsWith("http")) {
                    return new java.net.URL(new java.net.URL(baseUrl), iconUrl).toString();
                }
                return iconUrl;
            }

            return "";
        } catch (Exception e) {
            return "";
        }
    }
}