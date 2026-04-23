package com.officeknowledgemap.service;

import com.officeknowledgemap.model.Endorsement;
import com.officeknowledgemap.model.Skill;
import com.officeknowledgemap.model.User;
import com.officeknowledgemap.repository.EndorsementRepository;
import com.officeknowledgemap.repository.SkillRepository;
import com.officeknowledgemap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {
    
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final EndorsementRepository endorsementRepository;
    
    @Transactional(readOnly = true)
    public byte[] exportUsers() throws IOException {
        List<User> users = userRepository.findAll();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ID", "Username", "Name", "Role", "Contact Info", "Main Team", "Sub Team", "Created At"))) {
            
            for (User user : users) {
                csvPrinter.printRecord(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getRole(),
                        user.getContactInfo(),
                        user.getMainTeam() != null ? user.getMainTeam().getName() : "",
                        user.getSubTeam() != null ? user.getSubTeam().getName() : "",
                        user.getCreatedAt()
                );
            }
            
            csvPrinter.flush();
        }
        
        return outputStream.toByteArray();
    }
    
    @Transactional(readOnly = true)
    public byte[] exportSkills() throws IOException {
        List<Skill> skills = skillRepository.findAll();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ID", "Skill Name", "Type", "Proficiency", "User", "Years of Experience", "Endorsement Count", "Created At"))) {
            
            for (Skill skill : skills) {
                Long endorsementCount = endorsementRepository.countBySkillId(skill.getId());
                
                csvPrinter.printRecord(
                        skill.getId(),
                        skill.getName(),
                        skill.getType(),
                        skill.getProficiency(),
                        skill.getUser().getName(),
                        skill.getYearsOfExperience(),
                        endorsementCount,
                        skill.getCreatedAt()
                );
            }
            
            csvPrinter.flush();
        }
        
        return outputStream.toByteArray();
    }
    
    @Transactional(readOnly = true)
    public byte[] exportEndorsements() throws IOException {
        List<Endorsement> endorsements = endorsementRepository.findAll();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("ID", "Endorser", "Skill Owner", "Skill Name", "Skill Type", "Comment", "Created At"))) {
            
            for (Endorsement endorsement : endorsements) {
                csvPrinter.printRecord(
                        endorsement.getId(),
                        endorsement.getEndorser().getName(),
                        endorsement.getSkill().getUser().getName(),
                        endorsement.getSkill().getName(),
                        endorsement.getSkill().getType(),
                        endorsement.getComment() != null ? endorsement.getComment() : "",
                        endorsement.getCreatedAt()
                );
            }
            
            csvPrinter.flush();
        }
        
        return outputStream.toByteArray();
    }
    
    @Transactional(readOnly = true)
    public byte[] exportAll() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("User ID", "User Name", "Role", "Team", "Skill Name", "Skill Type", "Proficiency", "Endorsement Count"))) {
            
            List<User> users = userRepository.findAll();
            
            for (User user : users) {
                List<Skill> userSkills = skillRepository.findByUserId(user.getId());
                
                if (userSkills.isEmpty()) {
                    csvPrinter.printRecord(
                            user.getId(),
                            user.getName(),
                            user.getRole(),
                            user.getMainTeam() != null ? user.getMainTeam().getName() : "",
                            "",
                            "",
                            "",
                            0
                    );
                } else {
                    for (Skill skill : userSkills) {
                        Long endorsementCount = endorsementRepository.countBySkillId(skill.getId());
                        
                        csvPrinter.printRecord(
                                user.getId(),
                                user.getName(),
                                user.getRole(),
                                user.getMainTeam() != null ? user.getMainTeam().getName() : "",
                                skill.getName(),
                                skill.getType(),
                                skill.getProficiency(),
                                endorsementCount
                        );
                    }
                }
            }
            
            csvPrinter.flush();
        }
        
        return outputStream.toByteArray();
    }
}
