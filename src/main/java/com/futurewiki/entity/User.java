package com.futurewiki.entity;

import com.futurewiki.entity.enums.Role;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdAt;
}
