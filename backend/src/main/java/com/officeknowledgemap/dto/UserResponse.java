package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private UserRole role;
    private String contactInfo;
    private String photoUrl;
    private Long mainTeamId;
    private String mainTeamName;
    private Long subTeamId;
    private String subTeamName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
