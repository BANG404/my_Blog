package com.ibat.myblog.Util;

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
                        "You are a media recognizer, tell me the type of media (music, film and television) from the text, tell me the information of the media (name, author), separated by commas."),
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