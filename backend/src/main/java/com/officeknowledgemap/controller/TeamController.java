package com.officeknowledgemap.controller;

import com.officeknowledgemap.dto.TeamHierarchyResponse;
import com.officeknowledgemap.dto.TeamRequest;
import com.officeknowledgemap.dto.TeamResponse;
import com.officeknowledgemap.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService teamService;
    
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }
    
    @GetMapping("/hierarchy")
    public ResponseEntity<List<TeamHierarchyResponse>> getTeamHierarchy() {
        return ResponseEntity.ok(teamService.getTeamHierarchy());
    }
    
    @GetMapping("/main")
    public ResponseEntity<List<TeamResponse>> getMainTeams() {
        return ResponseEntity.ok(teamService.getMainTeams());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }
    
    @GetMapping("/{id}/sub-teams")
    public ResponseEntity<List<TeamResponse>> getSubTeams(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getSubTeams(id));
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(request));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable Long id, 
                                                   @Valid @RequestBody TeamRequest request) {
        return ResponseEntity.ok(teamService.updateTeam(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
