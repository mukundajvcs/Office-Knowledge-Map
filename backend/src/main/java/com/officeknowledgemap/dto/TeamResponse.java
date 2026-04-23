package com.officeknowledgemap.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TeamResponse {
    private Long id;
    private String name;
    private String description;
    private Long parentTeamId;
    private String parentTeamName;
    private Boolean isMainTeam;
    private List<TeamResponse> subTeams;
    private Integer memberCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
