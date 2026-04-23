package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.UserRole;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String contactInfo;
    private String photoUrl;
    private UserRole role;
    private Long mainTeamId;
    private Long subTeamId;
}
