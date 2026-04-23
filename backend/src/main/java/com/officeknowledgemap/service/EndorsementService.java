package com.officeknowledgemap.service;

import com.officeknowledgemap.dto.EndorsementRequest;
import com.officeknowledgemap.dto.EndorsementResponse;
import com.officeknowledgemap.model.Endorsement;
import com.officeknowledgemap.model.Skill;
import com.officeknowledgemap.model.User;
import com.officeknowledgemap.model.UserRole;
import com.officeknowledgemap.repository.EndorsementRepository;
import com.officeknowledgemap.repository.SkillRepository;
import com.officeknowledgemap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EndorsementService {
    
    private final EndorsementRepository endorsementRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<EndorsementResponse> getAllEndorsements() {
        return endorsementRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public EndorsementResponse getEndorsementById(Long id) {
        Endorsement endorsement = endorsementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endorsement not found"));
        return mapToResponse(endorsement);
    }
    
    @Transactional(readOnly = true)
    public List<EndorsementResponse> getEndorsementsBySkillId(Long skillId) {
        return endorsementRepository.findBySkillId(skillId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EndorsementResponse> getEndorsementsByUserId(Long userId) {
        return endorsementRepository.findBySkillUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EndorsementResponse> getEndorsementsGivenByUser(Long userId) {
        return endorsementRepository.findByEndorserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public EndorsementResponse createEndorsement(Long endorserId, EndorsementRequest request) {
        User endorser = userRepository.findById(endorserId)
                .orElseThrow(() -> new RuntimeException("Endorser not found"));
        
        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        
        User skillOwner = skill.getUser();
        
        // Validation: Cannot endorse own skills
        if (skillOwner.getId().equals(endorserId)) {
            throw new RuntimeException("Cannot endorse your own skills");
        }
        
        // Validation: Check role-based endorsement permission
        if (!canEndorse(endorser.getRole(), skillOwner.getRole())) {
            throw new RuntimeException(
                String.format("A %s cannot endorse a %s", 
                    endorser.getRole(), skillOwner.getRole())
            );
        }
        
        // Validation: Cannot endorse the same skill twice
        if (endorsementRepository.existsByEndorserIdAndSkillId(endorserId, request.getSkillId())) {
            throw new RuntimeException("You have already endorsed this skill");
        }
        
        Endorsement endorsement = new Endorsement();
        endorsement.setEndorser(endorser);
        endorsement.setSkill(skill);
        endorsement.setComment(request.getComment());
        
        endorsement = endorsementRepository.save(endorsement);
        return mapToResponse(endorsement);
    }
    
    @Transactional
    public void deleteEndorsement(Long id) {
        if (!endorsementRepository.existsById(id)) {
            throw new RuntimeException("Endorsement not found");
        }
        endorsementRepository.deleteById(id);
    }
    
    /**
     * Validates if an endorser with a given role can endorse a user with another role.
     * Role hierarchy:
     * - ADMIN can endorse MANAGER, TEAM_LEAD, EMPLOYEE
     * - MANAGER can endorse TEAM_LEAD, EMPLOYEE
     * - TEAM_LEAD can endorse EMPLOYEE
     * - EMPLOYEE cannot endorse anyone
     */
    private boolean canEndorse(UserRole endorserRole, UserRole endorsedRole) {
        switch (endorserRole) {
            case ADMIN:
                // Admin can endorse Manager, Team Lead, and Employee
                return endorsedRole == UserRole.MANAGER 
                    || endorsedRole == UserRole.TEAM_LEAD 
                    || endorsedRole == UserRole.EMPLOYEE;
                
            case MANAGER:
                // Manager can endorse Team Lead and Employee
                return endorsedRole == UserRole.TEAM_LEAD 
                    || endorsedRole == UserRole.EMPLOYEE;
                
            case TEAM_LEAD:
                // Team Lead can endorse Employee only
                return endorsedRole == UserRole.EMPLOYEE;
                
            case EMPLOYEE:
                // Employee cannot endorse anyone
                return false;
                
            default:
                return false;
        }
    }
    
    private EndorsementResponse mapToResponse(Endorsement endorsement) {
        EndorsementResponse response = new EndorsementResponse();
        response.setId(endorsement.getId());
        response.setEndorserId(endorsement.getEndorser().getId());
        response.setEndorserName(endorsement.getEndorser().getName());
        response.setSkillId(endorsement.getSkill().getId());
        response.setSkillName(endorsement.getSkill().getName());
        response.setSkillType(endorsement.getSkill().getType());
        response.setSkillProficiency(endorsement.getSkill().getProficiency());
        response.setSkillOwnerId(endorsement.getSkill().getUser().getId());
        response.setSkillOwnerName(endorsement.getSkill().getUser().getName());
        // Set aliases for frontend compatibility
        response.setEndorsedUserId(endorsement.getSkill().getUser().getId());
        response.setEndorsedUserName(endorsement.getSkill().getUser().getName());
        response.setComment(endorsement.getComment());
        response.setCreatedAt(endorsement.getCreatedAt());
        return response;
    }
}
