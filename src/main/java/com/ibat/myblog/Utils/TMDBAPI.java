package com.ibat.myblog.Utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class TMDBAPI {
    private static final String API_KEY = "c9db3026e66493fde580c4221a32456d";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public TMDBAPI() {
        this.client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
        this.mapper = new ObjectMapper();
    }

    public String getMoviePoster(String movieTitle, String size) throws IOException {
        if (movieTitle == null || movieTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be null or empty");
        }
        if (!isValidSize(size)) {
            throw new IOException("Invalid poster size");
        }
        // size 可以是 "w500", "original" 等
        String searchUrl = String.format("%s/search/movie?api_key=%s&query=%s",
                BASE_URL, API_KEY, movieTitle);

        Request request = new Request.Builder()
                .url(searchUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response " + response);
            }

            JsonNode rootNode = null;
            if (response.body() != null) {
                rootNode = mapper.readTree(response.body().string());
            }
            JsonNode resultsNode = null;
            if (rootNode != null) {
                resultsNode = rootNode.get("results");
            }

            if (resultsNode != null && !resultsNode.isEmpty()) {
                String posterPath = resultsNode.get(0).get("poster_path").asText();
                return IMAGE_BASE_URL + size + posterPath;
            }
        }
        return null;
    }

    private boolean isValidSize(String size) {
        // Add logic to validate size
        return size.matches("w[0-9]+") || size.equals("original");
    }

    // 获取电影详细信息的方法
    public JsonNode getMovieDetails(int movieId) throws IOException {
        String detailUrl = String.format("%s/movie/%d?api_key=%s",
                BASE_URL, movieId, API_KEY);

        Request request = new Request.Builder()
                .url(detailUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response " + response);
            }

            return mapper.readTree(response.body().string());
        }
    }

    private <T> T executeWithRetry(Callable<T> task, int maxRetries) throws IOException {
        int retryCount = 0;
        while (true) {
            try {
                return task.call();
            } catch (Exception e) {
                if (++retryCount >= maxRetries) {
                    throw new IOException("Failed after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(1000 * retryCount); // 递增延迟
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted during retry", e);
                }
            }
        }
    }
}