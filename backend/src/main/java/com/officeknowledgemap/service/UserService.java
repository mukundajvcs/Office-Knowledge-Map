package com.officeknowledgemap.service;

import com.officeknowledgemap.dto.UserResponse;
import com.officeknowledgemap.dto.UserUpdateRequest;
import com.officeknowledgemap.model.Endorsement;
import com.officeknowledgemap.model.Skill;
import com.officeknowledgemap.model.Team;
import com.officeknowledgemap.model.User;
import com.officeknowledgemap.model.UserRole;
import com.officeknowledgemap.repository.EndorsementRepository;
import com.officeknowledgemap.repository.SkillRepository;
import com.officeknowledgemap.repository.TeamRepository;
import com.officeknowledgemap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final SkillRepository skillRepository;
    private final EndorsementRepository endorsementRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String UPLOAD_DIR = "images/";
    
    @Transactional
    public UserResponse createUser(com.officeknowledgemap.dto.UserCreateRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setContactInfo(request.getContactInfo());
        user.setRole(request.getRole());
        user.setPhotoUrl(request.getPhotoUrl());
        
        if (request.getMainTeamId() != null) {
            Team mainTeam = teamRepository.findById(request.getMainTeamId())
                    .orElseThrow(() -> new RuntimeException("Main team not found"));
            user.setMainTeam(mainTeam);
        }
        
        if (request.getSubTeamId() != null) {
            Team subTeam = teamRepository.findById(request.getSubTeamId())
                    .orElseThrow(() -> new RuntimeException("Sub team not found"));
            user.setSubTeam(subTeam);
        }
        
        user = userRepository.save(user);
        return mapToResponse(user);
    }
    
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }
    
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getContactInfo() != null) {
            user.setContactInfo(request.getContactInfo());
        }
        if (request.getPhotoUrl() != null) {
            user.setPhotoUrl(request.getPhotoUrl());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getMainTeamId() != null) {
            Team mainTeam = teamRepository.findById(request.getMainTeamId())
                    .orElseThrow(() -> new RuntimeException("Main team not found"));
            user.setMainTeam(mainTeam);
        }
        if (request.getSubTeamId() != null) {
            Team subTeam = teamRepository.findById(request.getSubTeamId())
                    .orElseThrow(() -> new RuntimeException("Sub team not found"));
            user.setSubTeam(subTeam);
        }
        
        user = userRepository.save(user);
        return mapToResponse(user);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        
        // Cascade delete: Remove all associated data before deleting user
        
        // 1. Get all skills owned by this user
        List<Skill> userSkills = skillRepository.findByUserId(id);
        
        // 2. Delete all endorsements for those skills
        for (Skill skill : userSkills) {
            endorsementRepository.deleteBySkillId(skill.getId());
        }
        
        // 3. Delete all endorsements given by this user
        endorsementRepository.deleteByEndorserId(id);
        
        // 4. Delete all skills owned by this user
        skillRepository.deleteByUserId(id);
        
        // 5. Finally, delete the user
        userRepository.deleteById(id);
    }
    
    @Transactional
    public UserResponse assignTeams(Long userId, Long mainTeamId, Long subTeamId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (mainTeamId != null) {
            Team mainTeam = teamRepository.findById(mainTeamId)
                    .orElseThrow(() -> new RuntimeException("Main team not found"));
            user.setMainTeam(mainTeam);
        }
        
        if (subTeamId != null) {
            Team subTeam = teamRepository.findById(subTeamId)
                    .orElseThrow(() -> new RuntimeException("Sub team not found"));
            user.setSubTeam(subTeam);
        }
        
        user = userRepository.save(user);
        return mapToResponse(user);
    }
    @Transactional
    public UserResponse uploadPhoto(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (file.isEmpty()) {
            throw new RuntimeException("Please select a file to upload");
        }
        
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                    : "";
            String filename = userId + "_" + UUID.randomUUID().toString() + extension;
            
            // Save file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Update user's photo URL
            String photoUrl = "/images/" + filename;
            user.setPhotoUrl(photoUrl);
            user = userRepository.save(user);
            
            return mapToResponse(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }
    
    
    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setRole(user.getRole());
        response.setContactInfo(user.getContactInfo());
        response.setPhotoUrl(user.getPhotoUrl());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        
        if (user.getMainTeam() != null) {
            response.setMainTeamId(user.getMainTeam().getId());
            response.setMainTeamName(user.getMainTeam().getName());
        }
        
        if (user.getSubTeam() != null) {
            response.setSubTeamId(user.getSubTeam().getId());
            response.setSubTeamName(user.getSubTeam().getName());
        }
        
        return response;
    }
}
