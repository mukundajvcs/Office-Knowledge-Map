package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Contact info is required")
    private String contactInfo;
    
    @NotNull(message = "Role is required")
    private UserRole role;
    
    private String photoUrl;
    
    private Long mainTeamId;
    
    private Long subTeamId;
}
