package com.officeknowledgemap.service;

import com.officeknowledgemap.model.Team;
import com.officeknowledgemap.model.User;
import com.officeknowledgemap.model.UserRole;
import com.officeknowledgemap.repository.TeamRepository;
import com.officeknowledgemap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CsvService {
    
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public Map<String, Object> bulkUploadUsers(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {
            
            for (CSVRecord record : csvParser) {
                try {
                    String username = record.get("username");
                    String password = record.get("password");
                    String name = record.get("name");
                    String contactInfo = record.get("contactInfo");
                    String roleStr = record.get("role");
                    String photoUrl = record.isMapped("photoUrl") ? record.get("photoUrl") : null;
                    String mainTeamName = record.isMapped("mainTeam") ? record.get("mainTeam") : null;
                    String subTeamName = record.isMapped("subTeam") ? record.get("subTeam") : null;
                    
                    // Validate required fields
                    if (username == null || username.isEmpty() ||
                        password == null || password.isEmpty() ||
                        name == null || name.isEmpty() ||
                        contactInfo == null || contactInfo.isEmpty()) {
                        errors.add("Row " + record.getRecordNumber() + ": Missing required fields");
                        failureCount++;
                        continue;
                    }
                    
                    // Check if user already exists
                    if (userRepository.existsByUsername(username)) {
                        errors.add("Row " + record.getRecordNumber() + ": Username '" + username + "' already exists");
                        failureCount++;
                        continue;
                    }
                    
                    // Parse role
                    UserRole role;
                    try {
                        role = roleStr != null && !roleStr.isEmpty() 
                               ? UserRole.valueOf(roleStr.toUpperCase()) 
                               : UserRole.EMPLOYEE;
                    } catch (IllegalArgumentException e) {
                        errors.add("Row " + record.getRecordNumber() + ": Invalid role '" + roleStr + "'");
                        failureCount++;
                        continue;
                    }
                    
                    // Create user
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(passwordEncoder.encode(password));
                    user.setName(name);
                    user.setContactInfo(contactInfo);
                    user.setRole(role);
                    user.setPhotoUrl(photoUrl);
                    
                    // Assign teams if provided
                    if (mainTeamName != null && !mainTeamName.isEmpty()) {
                        Team mainTeam = teamRepository.findAll().stream()
                                .filter(t -> t.getName().equalsIgnoreCase(mainTeamName))
                                .findFirst()
                                .orElse(null);
                        if (mainTeam != null) {
                            user.setMainTeam(mainTeam);
                        }
                    }
                    
                    if (subTeamName != null && !subTeamName.isEmpty()) {
                        Team subTeam = teamRepository.findAll().stream()
                                .filter(t -> t.getName().equalsIgnoreCase(subTeamName))
                                .findFirst()
                                .orElse(null);
                        if (subTeam != null) {
                            user.setSubTeam(subTeam);
                        }
                    }
                    
                    userRepository.save(user);
                    successCount++;
                    
                } catch (Exception e) {
                    errors.add("Row " + record.getRecordNumber() + ": " + e.getMessage());
                    failureCount++;
                }
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error processing CSV file: " + e.getMessage());
            return result;
        }
        
        result.put("success", true);
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("errors", errors);
        result.put("message", "Imported " + successCount + " users successfully. " + failureCount + " failed.");
        
        return result;
    }
}
