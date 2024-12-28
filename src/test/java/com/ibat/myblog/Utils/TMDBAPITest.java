package com.ibat.myblog.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;





public class TMDBAPITest {
    private TMDBAPI tmdbApi;
    private MockWebServer mockWebServer;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mapper = new ObjectMapper();
        tmdbApi = new TMDBAPI();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("Should return poster URL when movie exists")
    void getMoviePoster_WithValidMovie_ReturnsPosterUrl() throws IOException {
        // Arrange
        String expectedPath = "/path/to/poster.jpg";
        String jsonResponse = "{\"results\": [{\"poster_path\": \"" + expectedPath + "\"}]}";
        
        // Act
        String posterUrl = tmdbApi.getMoviePoster("The Matrix", "w500");

        System.out.println(posterUrl);
    }

    @Test
    @DisplayName("Should return null when movie not found")
    void getMoviePoster_WithNonExistentMovie_ReturnsNull() throws IOException {
        // Arrange
        String jsonResponse = "{\"results\": []}";
        
        // Act
        String posterUrl = tmdbApi.getMoviePoster("NonExistentMovie123456789", "w500");
        
        // Assert
        assertNull(posterUrl);
    }

    @Test
    @DisplayName("Should handle empty movie title")
    void getMoviePoster_WithEmptyTitle_ReturnsNull() throws IOException {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            tmdbApi.getMoviePoster("", "w500");
        });
    }

    @Test
    @DisplayName("Should handle API errors")
    @Timeout(5)
    void getMoviePoster_WithAPIError_ThrowsIOException() {
        assertThrows(IOException.class, () -> {
            tmdbApi.getMoviePoster("The Matrix", "invalid_size");
        });
    }
}