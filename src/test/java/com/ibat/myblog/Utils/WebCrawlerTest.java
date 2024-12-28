package com.ibat.myblog.Utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;


public class WebCrawlerTest {
    
    private static final String VALID_URL = "https://www.google.com/search?q=bad+boys+music&oq=bad+boys+music&gs_lcrp=EgZjaHJvbWUqCggAEAAY4wIYgAQyCggAEAAY4wIYgAQyBwgBEC4YgAQyBwgCEAAYgAQyBwgDEAAYgAQyBwgEEAAYgAQyBwgFEAAYgAQyBwgGEAAYgAQyBwgHEAAYgAQyBwgIEAAYgAQyBwgJEC4YgATSAQg2NzA1ajBqN6gCCLACAQ&sourceid=chrome&ie=UTF-8";
    private static final String INVALID_URL = "https://invalid.example.com";

    @Test
    @Timeout(10) // 设置超时时间为10秒
    public void testCrawlWithValidUrl() {
        try {
            String result = WebCrawler.crawl(VALID_URL);
            System.out.println(result);
            assertNotNull(result, "Crawl result should not be null");
            assertFalse(result.isEmpty(), "Crawl result should not be empty");
        } catch (Exception e) {
            fail("Should not throw exception for valid URL: " + e.getMessage());
        }
    }

    @Test
    public void testCrawlWithInvalidUrl() {
        Exception exception = assertThrows(Exception.class, () -> {
            WebCrawler.crawl(INVALID_URL);
        });
        assertNotNull(exception, "Should throw exception for invalid URL");
    }

    @Test
    public void testCrawlWithNullUrl() {
        assertThrows(IllegalArgumentException.class, () -> {
            WebCrawler.crawl(null);
        });
    }

    @Test
    @Timeout(15) // 设置超时时间为15秒，测试重试机制
    public void testCrawlRetryMechanism() {
        try {
            String result = WebCrawler.crawl(VALID_URL);
            assertNotNull(result, "Crawl result should not be null after retries");
        } catch (Exception e) {
            // 如果所有重试都失败，应该抛出异常
            assertTrue(e.getMessage().contains("Attempt"), 
                "Exception should contain retry attempt information");
        }
    }

    @Test
    public void testCrawlWithEmptyPools() {
        try {
            String result = WebCrawler.crawl(VALID_URL);
            assertNotNull(result, "Should work even with empty cookie/proxy pools");
        } catch (Exception e) {
            fail("Should not fail when pools are empty: " + e.getMessage());
        }
    }
}