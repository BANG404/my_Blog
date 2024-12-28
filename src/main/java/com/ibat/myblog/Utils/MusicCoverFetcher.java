package com.ibat.myblog.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class MusicCoverFetcher {
    // QQ音乐搜索API
    private static final String SEARCH_API = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp" +
            "?w=%s&format=json&p=1&n=1";

    // 封面图片URL模板
    private static final String ALBUM_COVER_URL =
            "https://y.gtimg.cn/music/photo_new/T002R300x300M000%s.jpg";

    public String getCoverUrl(String songName, String artistName) throws IOException {
        // 构建搜索关键词
        String keyword = artistName + " " + songName;
        String searchUrl = String.format(SEARCH_API,
                java.net.URLEncoder.encode(keyword, "UTF-8"));

        // 获取搜索结果
        JSONObject response = null;
        try {
            response = doHttpGet(searchUrl);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String albumMid = parseAlbumMid(response);

        if (albumMid != null) {
            return String.format(ALBUM_COVER_URL, albumMid);
        }
        return null;
    }

    private String parseAlbumMid(JSONObject response) {
        try {
            JSONObject data = response.getJSONObject("data");
            JSONArray song = data.getJSONObject("song")
                    .getJSONArray("list");
            if (song.length() > 0) {
                return song.getJSONObject(0)
                        .getString("albummid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject doHttpGet(String urlString) throws IOException, JSONException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)");

        // 读取响应并转换为JSONObject
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return new JSONObject(response.toString());
        }
    }
}
