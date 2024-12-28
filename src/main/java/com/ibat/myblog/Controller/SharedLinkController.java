package com.ibat.myblog.Controller;

import com.ibat.myblog.Model.SharedLink;
import com.ibat.myblog.Service.SharedLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "分享链接管理", description = "分享链接相关接口")
@RestController
@RequestMapping("/api/shared-links")
public class SharedLinkController {

    @Autowired
    private SharedLinkService sharedLinkService;

    @Operation(
        summary = "获取最新分享链接列表",
        description = "根据传入的limit参数获取指定数量的最新分享链接"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "获取成功"),
        @ApiResponse(responseCode = "400", description = "参数错误"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/latest")
    public ResponseEntity<List<SharedLink>> getLatestSharedLinks(
        @Parameter(description = "获取链接数量", example = "5")
        @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(sharedLinkService.getLatestSharedLinks(limit));
    }
}
