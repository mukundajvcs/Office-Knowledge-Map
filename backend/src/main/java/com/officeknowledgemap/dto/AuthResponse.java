package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String id;
    private String username;
    private String name;
    private UserRole role;
}
