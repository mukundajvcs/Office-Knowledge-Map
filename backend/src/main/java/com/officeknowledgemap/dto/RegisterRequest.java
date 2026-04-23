package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String contactInfo;
    
    private UserRole role = UserRole.EMPLOYEE;
    private String photoUrl;
}
