package com.officeknowledgemap.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamRequest {
    @NotBlank
    private String name;
    
    private String description;
    private Long parentTeamId;
    private Boolean isMainTeam = true;
}
