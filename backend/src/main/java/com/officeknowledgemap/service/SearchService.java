package com.officeknowledgemap.service;

import com.officeknowledgemap.dto.SearchRequest;
import com.officeknowledgemap.dto.SearchResultResponse;
import com.officeknowledgemap.dto.SkillResponse;
import com.officeknowledgemap.model.Skill;
import com.officeknowledgemap.model.User;
import com.officeknowledgemap.repository.EndorsementRepository;
import com.officeknowledgemap.repository.SkillRepository;
import com.officeknowledgemap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final EndorsementRepository endorsementRepository;
    
    @Transactional(readOnly = true)
    public List<SearchResultResponse> search(SearchRequest request) {
        List<User> users = new ArrayList<>();
        
        // Search by skill name
        if (request.getSkillName() != null && !request.getSkillName().isEmpty()) {
            List<Skill> skills = skillRepository.searchSkills(
                    request.getSkillName(),
                    request.getSkillType(),
                    request.getProficiency(),
                    null
            );
            users = skills.stream()
                    .map(Skill::getUser)
                    .distinct()
                    .collect(Collectors.toList());
        } else {
            users = userRepository.findAll();
        }
        
        // Filter by team
        if (request.getTeamId() != null) {
            final Long teamId = request.getTeamId();
            users = users.stream()
                    .filter(u -> (u.getMainTeam() != null && u.getMainTeam().getId().equals(teamId)) ||
                                 (u.getSubTeam() != null && u.getSubTeam().getId().equals(teamId)))
                    .collect(Collectors.toList());
        }
        
        // Filter by role
        if (request.getRole() != null) {
            users = users.stream()
                    .filter(u -> u.getRole().equals(request.getRole()))
                    .collect(Collectors.toList());
        }
        
        // Filter by user name
        if (request.getUserName() != null && !request.getUserName().isEmpty()) {
            final String searchName = request.getUserName().toLowerCase();
            users = users.stream()
                    .filter(u -> u.getName().toLowerCase().contains(searchName) ||
                                 u.getUsername().toLowerCase().contains(searchName))
                    .collect(Collectors.toList());
        }
        
        // Convert to response and rank by endorsement count
        List<SearchResultResponse> results = users.stream()
                .map(this::mapToSearchResult)
                .sorted(Comparator.comparingInt(SearchResultResponse::getTotalEndorsements).reversed())
                .collect(Collectors.toList());
        
        return results;
    }
    
    private SearchResultResponse mapToSearchResult(User user) {
        SearchResultResponse response = new SearchResultResponse();
        response.setUserId(user.getId());
        response.setUserName(user.getName());
        response.setRole(user.getRole());
        response.setContactInfo(user.getContactInfo());
        response.setPhotoUrl(user.getPhotoUrl());
        
        if (user.getMainTeam() != null) {
            response.setMainTeamName(user.getMainTeam().getName());
        }
        
        if (user.getSubTeam() != null) {
            response.setSubTeamName(user.getSubTeam().getName());
        }
        
        // Get user's skills
        List<Skill> skills = skillRepository.findByUserId(user.getId());
        List<SkillResponse> skillResponses = skills.stream()
                .map(this::mapSkillToResponse)
                .collect(Collectors.toList());
        response.setSkills(skillResponses);
        
        // Get total endorsements
        Long totalEndorsements = endorsementRepository.countByUserId(user.getId());
        response.setTotalEndorsements(totalEndorsements.intValue());
        
        return response;
    }
    
    private SkillResponse mapSkillToResponse(Skill skill) {
        SkillResponse response = new SkillResponse();
        response.setId(skill.getId());
        response.setName(skill.getName());
        response.setType(skill.getType());
        response.setProficiency(skill.getProficiency());
        response.setDescription(skill.getDescription());
        response.setYearsOfExperience(skill.getYearsOfExperience());
        
        Long endorsementCount = endorsementRepository.countBySkillId(skill.getId());
        response.setEndorsementCount(endorsementCount.intValue());
        
        return response;
    }
}
