package com.ibat.myblog.Service;

import com.ibat.myblog.Model.LoginRequest;
import com.ibat.myblog.Model.LoginResponse;
import com.ibat.myblog.Model.RegisterRequest;
import com.ibat.myblog.Model.User;
import com.ibat.myblog.Model.UserInfo;
import com.ibat.myblog.Repository.UserRepository;
import com.ibat.myblog.Security.JwtTokenProvider;

import com.ibat.myblog.Model.UpdateUserRequest;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        String token = jwtTokenProvider.createToken(user.getUsername());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserInfo(convertToUserInfo(user));

        return response;
    }

    private UserInfo convertToUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(user.getUsername());
        userInfo.setBlogName(user.getBlogName());
        userInfo.setEmail(user.getEmail());
        userInfo.setBio(user.getBio());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setWechatId(user.getWechatId());
        return userInfo;
    }
    public UserInfo register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setBlogName(request.getBlogName());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 返回用户信息
        return convertToUserInfo(savedUser);
    }

    public LoginResponse registerAndLogin(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setBlogName(request.getBlogName());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 生成token
        String token = jwtTokenProvider.createToken(savedUser.getUsername());
        
        // 创建登录响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserInfo(convertToUserInfo(savedUser));
        
        return response;
    }

    public boolean checkUserExists() {
        return userRepository.count() > 0;
    }

    public UserInfo updateUser(UpdateUserRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 更新用户信息
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getBlogName() != null) {
            user.setBlogName(request.getBlogName());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getWechatId() != null) {
            user.setWechatId(request.getWechatId());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user.setUpdateAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        
        return convertToUserInfo(savedUser);
    }

    public UserInfo updateUser(String username, UpdateUserRequest request) {
        // 获取当前登录用户
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // 权限检查：只有管理员或用户自己可以修改信息
        if (!currentUsername.equals(username)) {
            throw new RuntimeException("无权限修改其他用户信息");
        }

        // 查找要更新的用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 更新用户信息
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getBlogName() != null) {
            user.setBlogName(request.getBlogName());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getWechatId() != null) {
            user.setWechatId(request.getWechatId());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user.setUpdateAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        
        return convertToUserInfo(savedUser);
    }

    public UserInfo getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToUserInfo(user);
    }

    public UserInfo getBlogOwner() {
        // 获取ID最小（最早注册）的用户作为博主
        User blogOwner = userRepository.findFirstByOrderByUserIdAsc()
            .orElseThrow(() -> new RuntimeException("未找到博主信息"));
        return convertToUserInfo(blogOwner);
    }

    public Integer findUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return user.getUserId();
    }
}
