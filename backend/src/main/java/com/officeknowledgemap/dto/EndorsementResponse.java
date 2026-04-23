package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.SkillProficiency;
import com.officeknowledgemap.model.SkillType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EndorsementResponse {
    private Long id;
    private Long endorserId;
    private String endorserName;
    private Long skillId;
    private String skillName;
    private SkillType skillType;
    private SkillProficiency skillProficiency;
    private Long skillOwnerId;
    private String skillOwnerName;
    // Aliases for frontend compatibility
    private Long endorsedUserId;
    private String endorsedUserName;
    private String comment;
    private LocalDateTime createdAt;
}
