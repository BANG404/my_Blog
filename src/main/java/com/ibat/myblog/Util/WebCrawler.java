package com.ibat.myblog.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);
    private static final Random random = new Random();
    
    // Cookie池
    private static final List<String> COOKIE_POOL = new ArrayList<>();
    // 代理池
    private static final List<String> PROXY_POOL = new ArrayList<>();
    
    // 配置项
    private static final int MAX_RETRY = 3;
    private static final int MIN_DELAY = 1000;
    private static final int MAX_DELAY = 3000;

    public static String crawl(String url) throws Exception {
        int retryCount = 0;
        while (retryCount < MAX_RETRY) {
            try {
                // 随机延迟
                Thread.sleep(random.nextInt(MAX_DELAY - MIN_DELAY) + MIN_DELAY);
                
                HttpGet httpGet = new HttpGet(url);
                configureRequest(httpGet);
                
                // 设置代理
                HttpHost proxy = getRandomProxy();
                RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .setConnectTimeout(5000)
                    .setSocketTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
                httpGet.setConfig(config);
                
                try (CloseableHttpClient httpClient = HttpClients.createDefault();
                     CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        return EntityUtils.toString(entity, "UTF-8");
                    }
                }
            } catch (Exception e) {
                logger.error("Attempt " + (retryCount + 1) + " failed for URL: " + url, e);
                retryCount++;
                if (retryCount == MAX_RETRY) {
                    throw e;
                }
            }
        }
        return null;
    }

    private static void configureRequest(HttpGet httpGet) {
        // 随机Cookie
        String cookie = getRandomCookie();
        
        httpGet.setHeader("User-Agent", getRandomUserAgent());
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("Cookie", cookie);
        httpGet.setHeader("Cache-Control", "max-age=0");
        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("Referer", "https://www.google.com/");
    }

    private static String getRandomCookie() {
        if (COOKIE_POOL.isEmpty()) {
            return "";
        }
        return COOKIE_POOL.get(random.nextInt(COOKIE_POOL.size()));
    }

    private static HttpHost getRandomProxy() {
        if (PROXY_POOL.isEmpty()) {
            return null;
        }
        String proxy = PROXY_POOL.get(random.nextInt(PROXY_POOL.size()));
        String[] parts = proxy.split(":");
        return new HttpHost(parts[0], Integer.parseInt(parts[1]));
    }

    private static String getRandomUserAgent() {
        List<String> userAgents = List.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
        );
        return userAgents.get(random.nextInt(userAgents.size()));
    }
}