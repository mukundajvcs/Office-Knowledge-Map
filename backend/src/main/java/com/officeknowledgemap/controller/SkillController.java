package com.officeknowledgemap.controller;

import com.officeknowledgemap.dto.SkillRequest;
import com.officeknowledgemap.dto.SkillResponse;
import com.officeknowledgemap.model.SkillProficiency;
import com.officeknowledgemap.model.SkillType;
import com.officeknowledgemap.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {
    
    private final SkillService skillService;
    
    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }
    
    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllSkillNames() {
        return ResponseEntity.ok(skillService.getAllSkillNames());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SkillResponse> getSkillById(@PathVariable Long id) {
        return ResponseEntity.ok(skillService.getSkillById(id));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SkillResponse>> getSkillsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(skillService.getSkillsByUserId(userId));
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<List<SkillResponse>> getSkillsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(skillService.getSkillsByUsername(username));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<SkillResponse>> searchSkills(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) SkillType type,
            @RequestParam(required = false) SkillProficiency proficiency) {
        return ResponseEntity.ok(skillService.searchSkills(name, type, proficiency));
    }
    
    @PostMapping("/user/{userId}")
    public ResponseEntity<SkillResponse> createSkill(@PathVariable Long userId,
                                                     @Valid @RequestBody SkillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(skillService.createSkill(userId, request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SkillResponse> updateSkill(@PathVariable Long id,
                                                     @Valid @RequestBody SkillRequest request) {
        return ResponseEntity.ok(skillService.updateSkill(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}
