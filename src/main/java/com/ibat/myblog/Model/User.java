package com.ibat.myblog.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 50)
    private String username;

    @Column(length = 255)
    private String password;

    @Column(length = 255)
    private String avatar;

    @Column(length = 100)
    private String email;

    @Column(length = 50)
    private String wechatId;

    @Column(columnDefinition = "text")
    private String bio;

    @Column(length = 255)
    private String blogName;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
