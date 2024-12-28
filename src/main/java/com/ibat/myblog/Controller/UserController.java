package com.ibat.myblog.Controller;

import com.ibat.myblog.Model.UpdateUserRequest;
import com.ibat.myblog.Model.UserInfo;
import com.ibat.myblog.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "用户信息管理相关接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "更新当前用户信息", description = "根据JWT token更新当前登录用户信息")
    @ApiResponse(responseCode = "200", description = "更新成功返回最新用户信息")
    @PutMapping("/profile")
    public ResponseEntity<UserInfo> updateCurrentUser(
            @Parameter(description = "用户更新信息", required = true)
            @RequestBody UpdateUserRequest updateRequest) {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo updatedUser = userService.updateUser(updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "获取博主信息", description = "获取博主（第一个注册用户）的详细信息")
    @ApiResponse(responseCode = "200", description = "成功返回博主信息")
    @GetMapping("/profile")
    public ResponseEntity<UserInfo> getCurrentUser() {
        UserInfo userInfo = userService.getBlogOwner(); // 修改为获取博主信息
        return ResponseEntity.ok(userInfo);
    }
}
