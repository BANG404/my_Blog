package com.ibat.myblog.Model;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String email;
    private String blogName;
    private String bio;
    private String avatar;
    private String wechatId;
    private String password;
}
