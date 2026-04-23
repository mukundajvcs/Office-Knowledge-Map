package com.officeknowledgemap.service;

import com.officeknowledgemap.dto.SkillRequest;
import com.officeknowledgemap.dto.SkillResponse;
import com.officeknowledgemap.model.Skill;
import com.officeknowledgemap.model.SkillProficiency;
import com.officeknowledgemap.model.SkillType;
import com.officeknowledgemap.model.User;
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
public class SkillService {
    
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final EndorsementRepository endorsementRepository;
    
    @Transactional(readOnly = true)
    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<String> getAllSkillNames() {
        return skillRepository.findAll().stream()
                .map(Skill::getName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public SkillResponse getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        return mapToResponse(skill);
    }
    
    @Transactional(readOnly = true)
    public List<SkillResponse> getSkillsByUserId(Long userId) {
        return skillRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SkillResponse> getSkillsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return skillRepository.findByUserId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SkillResponse> searchSkills(String name, SkillType type, SkillProficiency proficiency) {
        return skillRepository.searchSkills(name, type, proficiency, null).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public SkillResponse createSkill(Long userId, SkillRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (skillRepository.existsByUserIdAndName(userId, request.getName())) {
            throw new RuntimeException("Skill already exists for this user");
        }
        
        Skill skill = new Skill();
        skill.setName(request.getName());
        skill.setType(request.getType());
        skill.setProficiency(request.getProficiency());
        skill.setDescription(request.getDescription());
        skill.setYearsOfExperience(request.getYearsOfExperience());
        skill.setUser(user);
        
        skill = skillRepository.save(skill);
        return mapToResponse(skill);
    }
    
    @Transactional
    public SkillResponse updateSkill(Long id, SkillRequest request) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        
        skill.setName(request.getName());
        skill.setType(request.getType());
        skill.setProficiency(request.getProficiency());
        skill.setDescription(request.getDescription());
        skill.setYearsOfExperience(request.getYearsOfExperience());
        
        skill = skillRepository.save(skill);
        return mapToResponse(skill);
    }
    
    @Transactional
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new RuntimeException("Skill not found");
        }
        skillRepository.deleteById(id);
    }
    
    private SkillResponse mapToResponse(Skill skill) {
        SkillResponse response = new SkillResponse();
        response.setId(skill.getId());
        response.setName(skill.getName());
        response.setType(skill.getType());
        response.setProficiency(skill.getProficiency());
        response.setDescription(skill.getDescription());
        response.setYearsOfExperience(skill.getYearsOfExperience());
        response.setUserId(skill.getUser().getId());
        response.setUserName(skill.getUser().getName());
        response.setCreatedAt(skill.getCreatedAt());
        response.setUpdatedAt(skill.getUpdatedAt());
        
        // Get endorsement count
        Long endorsementCount = endorsementRepository.countBySkillId(skill.getId());
        response.setEndorsementCount(endorsementCount.intValue());
        
        return response;
    }
}
