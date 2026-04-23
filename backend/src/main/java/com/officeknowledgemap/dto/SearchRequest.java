package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.SkillProficiency;
import com.officeknowledgemap.model.SkillType;
import com.officeknowledgemap.model.UserRole;
import lombok.Data;

@Data
public class SearchRequest {
    private String skillName;
    private SkillType skillType;
    private SkillProficiency proficiency;
    private Long teamId;
    private UserRole role;
    private String userName;
}
