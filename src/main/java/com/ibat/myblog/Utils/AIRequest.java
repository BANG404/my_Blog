package com.ibat.myblog.Utils;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import lombok.Data;
import java.util.List;

@Component
public class AIRequest {

    private static final String API_URL = "https://api.luee.net/v1/chat/completions";
    private static final String AUTH_TOKEN = "sk-H76LbJpPJEZ8EiWtBc66F6Ed2cA64d01B30483547768C7Ee";
    private final RestTemplate restTemplate;

    public AIRequest() {
        this.restTemplate = new RestTemplate();
    }

    @Data
    static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    @Data
    static class RequestBody {
        private String model;
        private List<Message> messages;

        public RequestBody(String model, List<Message> messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    public ResponseEntity<String> makeRequest(String userContent) {
        if (userContent == null || userContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + AUTH_TOKEN);
        headers.set("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
        headers.set("Accept", "*/*");
        headers.set("Host", "api.luee.net");
        headers.set("Connection", "keep-alive");

        // Create messages
        List<Message> messages = List.of(
                new Message("system",
                        "你是一个媒体识别助手。请从文本中识别出音乐或影视作品信息，并按以下规则返回：\n" +
                        "1. 每个媒体信息占一行\n" +
                        "2. 信息格式：类型|标题|作者/演唱者\n" +
                        "3. 类型只能是：音乐、影视\n" +
                        "4. 分隔符统一使用竖线(|)\n" +
                        "5. 信息之间不要有空格\n" +
                        "6. 若找不到作者信息，询问 ai，如果查不到，请填写'未知'\n" +
                        "示例输出：\n" +
                        "音乐|起风了|买辣椒也用券\n" +
                        "影视|流浪地球|郭帆"),
                new Message("user", userContent));

        // Create request body
        RequestBody requestBody = new RequestBody("gpt-4o-mini", messages);

        // Create request entity
        HttpEntity<RequestBody> entity = new HttpEntity<>(requestBody, headers);

        try {
            return restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to make AI API request: " + e.getMessage(), e);
        }
    }
}