package com.ibat.myblog.Controller;

import com.ibat.myblog.Model.Media;
import com.ibat.myblog.Repository.MediaRepository;
import com.ibat.myblog.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "媒体管理", description = "音乐和视频管理接口")
@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaRepository mediaRepository;
    
    @Autowired
    private UserService userService;

    @Operation(summary = "获取最新媒体", description = "获取用户最新分享的指定类型媒体")
    @GetMapping("/latest")
    public ResponseEntity<Media> getLatestMedia(
            @Parameter(description = "用户名", required = true) 
            @RequestParam String username,
            @Parameter(description = "媒体类型(音乐/影视)", required = true) 
            @RequestParam String type) {
        
        // 参数验证
        if (!type.equals("音乐") && !type.equals("影视")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Integer userId = userService.findUserIdByUsername(username);
            Media latestMedia = mediaRepository
                .findFirstByUserIdAndTypeOrderByPublishDateDesc(userId, type)
                .orElse(null);
            return ResponseEntity.ok(latestMedia);
                    
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}