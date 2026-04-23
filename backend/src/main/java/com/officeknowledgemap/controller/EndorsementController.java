package com.officeknowledgemap.controller;

import com.officeknowledgemap.dto.EndorsementRequest;
import com.officeknowledgemap.dto.EndorsementResponse;
import com.officeknowledgemap.service.EndorsementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/endorsements")
@RequiredArgsConstructor
public class EndorsementController {
    
    private final EndorsementService endorsementService;
    
    @GetMapping
    public ResponseEntity<List<EndorsementResponse>> getAllEndorsements() {
        return ResponseEntity.ok(endorsementService.getAllEndorsements());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EndorsementResponse> getEndorsementById(@PathVariable Long id) {
        return ResponseEntity.ok(endorsementService.getEndorsementById(id));
    }
    
    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<EndorsementResponse>> getEndorsementsBySkillId(@PathVariable Long skillId) {
        return ResponseEntity.ok(endorsementService.getEndorsementsBySkillId(skillId));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EndorsementResponse>> getEndorsementsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(endorsementService.getEndorsementsByUserId(userId));
    }
    
    @GetMapping("/given-by/{userId}")
    public ResponseEntity<List<EndorsementResponse>> getEndorsementsGivenByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(endorsementService.getEndorsementsGivenByUser(userId));
    }
    
    @PostMapping("/endorser/{endorserId}")
    public ResponseEntity<EndorsementResponse> createEndorsement(@PathVariable Long endorserId,
                                                                 @Valid @RequestBody EndorsementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(endorsementService.createEndorsement(endorserId, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndorsement(@PathVariable Long id) {
        endorsementService.deleteEndorsement(id);
        return ResponseEntity.noContent().build();
    }
}
