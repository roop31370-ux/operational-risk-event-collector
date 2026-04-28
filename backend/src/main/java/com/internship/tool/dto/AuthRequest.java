package com.internship.tool.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;

    // 🔐 NEW FIELD (Day 10 - Roles)
    private String role; // USER or ADMIN
}