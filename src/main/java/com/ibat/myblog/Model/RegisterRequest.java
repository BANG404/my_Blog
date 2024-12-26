package com.ibat.myblog.Model;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String blogName;
    // private String bio;
    // private String avatar;
    // private String wechatId;
}