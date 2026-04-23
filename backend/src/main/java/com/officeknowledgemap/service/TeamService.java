package com.officeknowledgemap.service;

import com.officeknowledgemap.dto.TeamHierarchyResponse;
import com.officeknowledgemap.dto.TeamRequest;
import com.officeknowledgemap.dto.TeamResponse;
import com.officeknowledgemap.model.Team;
import com.officeknowledgemap.repository.TeamRepository;
import com.officeknowledgemap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {
    
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        return mapToResponse(team);
    }
    
    @Transactional(readOnly = true)
    public List<TeamResponse> getMainTeams() {
        return teamRepository.findByParentTeamIsNull().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TeamResponse> getSubTeams(Long parentTeamId) {
        return teamRepository.findByParentTeamId(parentTeamId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TeamHierarchyResponse> getTeamHierarchy() {
        // Get all main teams and build hierarchy
        return teamRepository.findByParentTeamIsNull().stream()
                .map(this::buildHierarchy)
                .collect(Collectors.toList());
    }
    
    private TeamHierarchyResponse buildHierarchy(Team team) {
        TeamHierarchyResponse response = new TeamHierarchyResponse();
        response.setId(team.getId());
        response.setName(team.getName());
        response.setDescription(team.getDescription());
        response.setIsMainTeam(team.getIsMainTeam());
        
        // Get member count
        int memberCount = userRepository.findByMainTeamId(team.getId()).size() + 
                         userRepository.findBySubTeamId(team.getId()).size();
        response.setMemberCount(memberCount);
        
        // Recursively build sub-teams
        List<TeamHierarchyResponse> subTeams = teamRepository.findByParentTeamId(team.getId()).stream()
                .map(this::buildHierarchy)
                .collect(Collectors.toList());
        response.setSubTeams(subTeams);
        
        return response;
    }
    
    @Transactional
    public TeamResponse createTeam(TeamRequest request) {
        Team team = new Team();
        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setIsMainTeam(request.getIsMainTeam());
        
        if (request.getParentTeamId() != null) {
            Team parentTeam = teamRepository.findById(request.getParentTeamId())
                    .orElseThrow(() -> new RuntimeException("Parent team not found"));
            team.setParentTeam(parentTeam);
            team.setIsMainTeam(false);
        }
        
        team = teamRepository.save(team);
        return mapToResponse(team);
    }
    
    @Transactional
    public TeamResponse updateTeam(Long id, TeamRequest request) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        
        team.setName(request.getName());
        team.setDescription(request.getDescription());
        
        if (request.getParentTeamId() != null) {
            Team parentTeam = teamRepository.findById(request.getParentTeamId())
                    .orElseThrow(() -> new RuntimeException("Parent team not found"));
            team.setParentTeam(parentTeam);
            team.setIsMainTeam(false);
        } else {
            team.setParentTeam(null);
            team.setIsMainTeam(true);
        }
        
        team = teamRepository.save(team);
        return mapToResponse(team);
    }
    
    @Transactional
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Team not found");
        }
        
        // Check if team has members
        long memberCount = userRepository.findByMainTeamId(id).size() + 
                          userRepository.findBySubTeamId(id).size();
        if (memberCount > 0) {
            throw new RuntimeException("Cannot delete team with members");
        }
        
        // Check if team has sub-teams
        List<Team> subTeams = teamRepository.findByParentTeamId(id);
        if (!subTeams.isEmpty()) {
            throw new RuntimeException("Cannot delete team with sub-teams");
        }
        
        teamRepository.deleteById(id);
    }
    
    private TeamResponse mapToResponse(Team team) {
        TeamResponse response = new TeamResponse();
        response.setId(team.getId());
        response.setName(team.getName());
        response.setDescription(team.getDescription());
        response.setIsMainTeam(team.getIsMainTeam());
        response.setCreatedAt(team.getCreatedAt());
        response.setUpdatedAt(team.getUpdatedAt());
        
        if (team.getParentTeam() != null) {
            response.setParentTeamId(team.getParentTeam().getId());
            response.setParentTeamName(team.getParentTeam().getName());
        }
        
        // Get sub-teams
        List<TeamResponse> subTeams = teamRepository.findByParentTeamId(team.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        response.setSubTeams(subTeams);
        
        // Get member count
        int memberCount = userRepository.findByMainTeamId(team.getId()).size() + 
                         userRepository.findBySubTeamId(team.getId()).size();
        response.setMemberCount(memberCount);
        
        return response;
    }
}
