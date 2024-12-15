package com.ibat.myblog.Model;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserInfo userInfo;
}