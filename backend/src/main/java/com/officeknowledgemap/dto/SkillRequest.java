package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.SkillProficiency;
import com.officeknowledgemap.model.SkillType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SkillRequest {
    @NotBlank
    private String name;
    
    @NotNull
    private SkillType type;
    
    @NotNull
    private SkillProficiency proficiency;
    
    private String description;
    private Integer yearsOfExperience = 0;
}
