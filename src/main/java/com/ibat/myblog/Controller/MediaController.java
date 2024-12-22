package com.ibat.myblog.Controller;

import com.ibat.myblog.Model.Media;
import com.ibat.myblog.Repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaRepository mediaRepository;

    @GetMapping("/latest")
    public ResponseEntity<Map<String, Media>> getLatestMedia(@RequestParam Integer userId) {
        Map<String, Media> result = new HashMap<>();

        // 获取最新音乐
        List<Media> latestMusic = mediaRepository.findTop1MusicByUserIdOrderByPublishDateDesc(userId);
        if (!latestMusic.isEmpty()) {
            result.put("music", latestMusic.get(0));
        }

        // 获取最新影视
        List<Media> latestMovie = mediaRepository.findTop1MovieByUserIdOrderByPublishDateDesc(userId);
        if (!latestMovie.isEmpty()) {
            result.put("movie", latestMovie.get(0));
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{type}/latest")
    public ResponseEntity<Media> getLatestByType(
            @PathVariable String type,
            @RequestParam Integer userId) {

        List<Media> mediaList;
        if ("music".equals(type)) {
            mediaList = mediaRepository.findTop1MusicByUserIdOrderByPublishDateDesc(userId);
        } else if ("movie".equals(type)) {
            mediaList = mediaRepository.findTop1MovieByUserIdOrderByPublishDateDesc(userId);
        } else {
            return ResponseEntity.badRequest().build();
        }

        return mediaList.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(mediaList.get(0));
    }
}