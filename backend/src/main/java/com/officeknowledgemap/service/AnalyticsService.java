package com.officeknowledgemap.service;

import com.officeknowledgemap.dto.AnalyticsResponse;
import com.officeknowledgemap.model.Endorsement;
import com.officeknowledgemap.model.Skill;
import com.officeknowledgemap.model.Team;
import com.officeknowledgemap.model.User;
import com.officeknowledgemap.repository.EndorsementRepository;
import com.officeknowledgemap.repository.SkillRepository;
import com.officeknowledgemap.repository.TeamRepository;
import com.officeknowledgemap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final EndorsementRepository endorsementRepository;
    private final TeamRepository teamRepository;
    
    @Transactional(readOnly = true)
    public AnalyticsResponse getAnalytics(String currentUsername) {
        // Get current user to determine filtering
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        boolean isSystemAdmin = "admin".equals(currentUsername);
        Long filterTeamId = null;
        
        // If not system admin, filter by user's team
        if (!isSystemAdmin) {
            // Use sub-team if assigned, otherwise use main team
            filterTeamId = currentUser.getSubTeam() != null 
                    ? currentUser.getSubTeam().getId() 
                    : (currentUser.getMainTeam() != null ? currentUser.getMainTeam().getId() : null);
        }
        
        // Top skills by occurrence with detailed statistics (filtered by team if applicable)
        List<Skill> allFilteredSkills;
        if (filterTeamId != null) {
            // Get skills only for the specific team
            allFilteredSkills = skillRepository.findByTeamId(filterTeamId);
        } else {
            // System admin sees all skills
            allFilteredSkills = skillRepository.findAll();
        }
        
        // Group skills by name and count
        Map<String, Long> skillNameCounts = allFilteredSkills.stream()
                .collect(Collectors.groupingBy(Skill::getName, Collectors.counting()));
        
        List<AnalyticsResponse.SkillStatistic> topSkills = skillNameCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    String skillName = entry.getKey();
                    Long userCount = entry.getValue();
                    
                    // Get all skills with this name from filtered set
                    List<Skill> skills = allFilteredSkills.stream()
                            .filter(s -> s.getName().equals(skillName))
                            .collect(Collectors.toList());
                    
                    // Get the most common skill type for this skill name
                    var skillType = skills.stream()
                            .map(Skill::getType)
                            .findFirst()
                            .orElse(null);
                    
                    // Count total endorsements for this skill
                    Long totalEndorsements = skills.stream()
                            .mapToLong(s -> endorsementRepository.countBySkillId(s.getId()))
                            .sum();
                    
                    // Calculate average proficiency
                    Map<String, Long> proficiencyCounts = skills.stream()
                            .collect(Collectors.groupingBy(
                                    s -> s.getProficiency().name(),
                                    Collectors.counting()
                            ));
                    
                    String avgProficiency = proficiencyCounts.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse("BEGINNER");
                    
                    return new AnalyticsResponse.SkillStatistic(
                            skillName, 
                            skillType, 
                            userCount, 
                            totalEndorsements, 
                            avgProficiency
                    );
                })
                .collect(Collectors.toList());
        
        // Team statistics with detailed skill information (filtered by team if applicable)
        List<Team> allTeams;
        if (filterTeamId != null) {
            // Get only the specific team for non-system admins
            teamRepository.findById(filterTeamId).ifPresent(team -> {});
            allTeams = teamRepository.findById(filterTeamId).stream().collect(Collectors.toList());
        } else {
            // System admin sees all teams
            allTeams = teamRepository.findAll();
        }
        
        List<AnalyticsResponse.TeamStatistic> teamStats = allTeams.stream()
                .map(team -> {
                    int memberCount = userRepository.findByMainTeamId(team.getId()).size() +
                                     userRepository.findBySubTeamId(team.getId()).size();
                    
                    List<Skill> teamSkills = skillRepository.findByTeamId(team.getId());
                    int totalSkills = teamSkills.size();
                    
                    // Top skills for this team
                    Map<String, Long> teamSkillNameCounts = teamSkills.stream()
                            .collect(Collectors.groupingBy(Skill::getName, Collectors.counting()));
                    
                    List<AnalyticsResponse.TopSkillItem> teamTopSkills = teamSkillNameCounts.entrySet().stream()
                            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                            .limit(5)
                            .map(e -> new AnalyticsResponse.TopSkillItem(e.getKey(), e.getValue().intValue()))
                            .collect(Collectors.toList());
                    
                    // Skill type distribution
                    Map<com.officeknowledgemap.model.SkillType, Long> typeDistribution = teamSkills.stream()
                            .collect(Collectors.groupingBy(Skill::getType, Collectors.counting()));
                    
                    List<AnalyticsResponse.SkillDistribution> skillDistribution = typeDistribution.entrySet().stream()
                            .map(e -> new AnalyticsResponse.SkillDistribution(e.getKey(), e.getValue().intValue()))
                            .collect(Collectors.toList());
                    
                    // Proficiency distribution
                    Map<com.officeknowledgemap.model.SkillProficiency, Long> proficiencyMap = teamSkills.stream()
                            .collect(Collectors.groupingBy(Skill::getProficiency, Collectors.counting()));
                    
                    List<AnalyticsResponse.ProficiencyDistribution> proficiencyDistribution = proficiencyMap.entrySet().stream()
                            .map(e -> new AnalyticsResponse.ProficiencyDistribution(e.getKey(), e.getValue().intValue()))
                            .collect(Collectors.toList());
                    
                    return new AnalyticsResponse.TeamStatistic(
                            team.getId(),
                            team.getName(),
                            memberCount,
                            totalSkills,
                            teamTopSkills,
                            skillDistribution,
                            proficiencyDistribution
                    );
                })
                .sorted(Comparator.comparingInt(AnalyticsResponse.TeamStatistic::getTotalSkills).reversed())
                .collect(Collectors.toList());
        
        // Endorsement trends (last 30 days, filtered by team if applicable)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<Endorsement> recentEndorsements = endorsementRepository.findRecentEndorsements(thirtyDaysAgo);
        
        // Filter endorsements to only those for filtered skills
        if (filterTeamId != null) {
            Set<Long> filteredSkillIds = allFilteredSkills.stream()
                    .map(Skill::getId)
                    .collect(Collectors.toSet());
            recentEndorsements = recentEndorsements.stream()
                    .filter(e -> filteredSkillIds.contains(e.getSkill().getId()))
                    .collect(Collectors.toList());
        }
        
        Map<String, Long> endorsementsByDay = recentEndorsements.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCreatedAt().toLocalDate().format(DateTimeFormatter.ISO_DATE),
                        Collectors.counting()
                ));
        
        List<AnalyticsResponse.EndorsementTrend> endorsementTrends = endorsementsByDay.entrySet().stream()
                .map(e -> new AnalyticsResponse.EndorsementTrend(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(AnalyticsResponse.EndorsementTrend::getDate))
                .collect(Collectors.toList());
        
        // Skill type distribution (using filtered skills)
        Map<String, Long> skillTypeDistribution = allFilteredSkills.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getType().name(),
                        Collectors.counting()
                ));
        
        // Proficiency distribution (using filtered skills)
        Map<String, Long> proficiencyDistribution = allFilteredSkills.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getProficiency().name(),
                        Collectors.counting()
                ));
        
        // Total counts (filtered by team if applicable)
        int totalUsers;
        int totalSkills;
        int totalEndorsements;
        
        if (filterTeamId != null) {
            // Count users in the specific team
            totalUsers = userRepository.findByMainTeamId(filterTeamId).size() +
                        userRepository.findBySubTeamId(filterTeamId).size();
            totalSkills = allFilteredSkills.size();
            totalEndorsements = (int) allFilteredSkills.stream()
                    .mapToLong(s -> endorsementRepository.countBySkillId(s.getId()))
                    .sum();
        } else {
            // System admin sees all counts
            totalUsers = (int) userRepository.count();
            totalSkills = (int) skillRepository.count();
            totalEndorsements = (int) endorsementRepository.count();
        }
        
        return new AnalyticsResponse(
                topSkills,
                teamStats,
                endorsementTrends,
                skillTypeDistribution,
                proficiencyDistribution,
                totalUsers,
                totalSkills,
                totalEndorsements
        );
    }
}
