package com.ibat.myblog.Service;

import com.ibat.myblog.Model.LoginRequest;
import com.ibat.myblog.Model.LoginResponse;
import com.ibat.myblog.Model.User;
import com.ibat.myblog.Model.UserInfo;
import com.ibat.myblog.Repository.UserRepository;
import com.ibat.myblog.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
        return userInfo;
    }
}
