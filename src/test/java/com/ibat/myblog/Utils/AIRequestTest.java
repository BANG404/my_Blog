package com.ibat.myblog.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;




public class AIRequestTest {

    private AIRequest aiRequest;

    @BeforeEach
    void setUp() {
        aiRequest = new AIRequest();
    }

    @Test
    void makeRequest_ValidInput_ReturnsResponse() {
        // Arrange
        String userContent = "Star Wars: A New Hope";

        // Act
        ResponseEntity<String> response = aiRequest.makeRequest(userContent);
        System.out.println(response);
        // Assert
        // assertNotNull(response);
        // assertEquals(HttpStatus.OK, response.getStatusCode());
        // assertNotNull(response.getBody());
    }

    @Test 
    void makeRequest_EmptyInput_ThrowsException() {
        // Arrange
        String userContent = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            aiRequest.makeRequest(userContent);
        });
    }

    @Test
    void makeRequest_NullInput_ThrowsException() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            aiRequest.makeRequest(null);
        });
    }
}
