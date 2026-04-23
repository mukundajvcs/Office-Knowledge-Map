package com.officeknowledgemap.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeamHierarchyResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isMainTeam;
    private Integer memberCount;
    private List<TeamHierarchyResponse> subTeams = new ArrayList<>();
}
