package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.SkillProficiency;
import com.officeknowledgemap.model.SkillType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SkillResponse {
    private Long id;
    private String name;
    private SkillType type;
    private SkillProficiency proficiency;
    private String description;
    private Integer yearsOfExperience;
    private Long userId;
    private String userName;
    private Integer endorsementCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
