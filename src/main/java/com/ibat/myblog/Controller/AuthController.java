package com.ibat.myblog.Controller;

import com.ibat.myblog.Model.LoginRequest;
import com.ibat.myblog.Model.LoginResponse;
import com.ibat.myblog.Model.RegisterRequest;
import com.ibat.myblog.Service.UserService;
import com.ibat.myblog.Utils.TokenValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ibat.myblog.Model.TokenValidationResult;


@Tag(name = "认证管理", description = "用户登录注册相关接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenValidator tokenValidator;

    @Operation(summary = "用户登录", description = "通过用户名密码登录获取token")
    @ApiResponse(responseCode = "200", description = "登录成功返回token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "登录信息", required = true) 
            @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "用户注册", description = "注册新用户并返回登录信息")
    @ApiResponse(responseCode = "200", description = "注册成功返回token和用户信息")
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @Parameter(description = "注册信息", required = true)
            @RequestBody RegisterRequest registerRequest) {
        LoginResponse response = userService.registerAndLogin(registerRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "检查用户存在", description = "检查数据库中是否存在用户")
    @ApiResponse(responseCode = "200", description = "返回检查结果")
    @GetMapping("/check-user-exists")
    public ResponseEntity<Boolean> checkUserExists() {
        boolean exists = userService.checkUserExists();
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "验证Token", description = "验证JWT token是否有效")
    @ApiResponse(responseCode = "200", description = "返回token验证结果")
    @GetMapping("/validate-token")
    public ResponseEntity<TokenValidationResult> validateToken(
            @Parameter(description = "JWT Token", required = true)
            @RequestParam String token) {
        TokenValidationResult result = tokenValidator.validateToken(token);
        return ResponseEntity.ok(result);
    }
}
