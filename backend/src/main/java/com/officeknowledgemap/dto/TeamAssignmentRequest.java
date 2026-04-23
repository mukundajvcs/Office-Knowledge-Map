package com.officeknowledgemap.dto;

import lombok.Data;

@Data
public class TeamAssignmentRequest {
    private Long userId;
    private Long mainTeamId;
    private Long subTeamId;
}
