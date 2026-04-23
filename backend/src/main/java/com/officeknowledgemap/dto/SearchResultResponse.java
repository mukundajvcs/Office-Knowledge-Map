package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultResponse {
    private Long userId;
    private String userName;
    private UserRole role;
    private String contactInfo;
    private String photoUrl;
    private String mainTeamName;
    private String subTeamName;
    private List<SkillResponse> skills;
    private Integer totalEndorsements;
}
