package com.officeknowledgemap.dto;

import com.officeknowledgemap.model.SkillProficiency;
import com.officeknowledgemap.model.SkillType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class AnalyticsResponse {
    private List<SkillStatistic> topSkills;
    private List<TeamStatistic> teamSummaries;
    private List<EndorsementTrend> endorsementTrends;
    private Map<String, Long> skillTypeDistribution;
    private Map<String, Long> proficiencyDistribution;
    private Integer totalUsers;
    private Integer totalSkills;
    private Integer totalEndorsements;
    
    @Data
    @AllArgsConstructor
    public static class SkillStatistic {
        private String skillName;
        private SkillType skillType;
        private Long userCount;
        private Long totalEndorsements;
        private String averageProficiency;
    }
    
    @Data
    @AllArgsConstructor
    public static class TeamStatistic {
        private Long teamId;
        private String teamName;
        private Integer totalMembers;
        private Integer totalSkills;
        private List<TopSkillItem> topSkills;
        private List<SkillDistribution> skillDistribution;
        private List<ProficiencyDistribution> proficiencyDistribution;
    }
    
    @Data
    @AllArgsConstructor
    public static class TopSkillItem {
        private String skillName;
        private Integer count;
    }
    
    @Data
    @AllArgsConstructor
    public static class SkillDistribution {
        private SkillType skillType;
        private Integer count;
    }
    
    @Data
    @AllArgsConstructor
    public static class ProficiencyDistribution {
        private SkillProficiency proficiency;
        private Integer count;
    }
    
    @Data
    @AllArgsConstructor
    public static class EndorsementTrend {
        private String date;
        private Long count;
    }
}
