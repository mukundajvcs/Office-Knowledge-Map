package com.officeknowledgemap.controller;

import com.officeknowledgemap.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {
    
    private final ExportService exportService;
    
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<byte[]> exportUsers() throws IOException {
        byte[] csvData = exportService.exportUsers();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
    
    @GetMapping("/skills")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<byte[]> exportSkills() throws IOException {
        byte[] csvData = exportService.exportSkills();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=skills.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
    
    @GetMapping("/endorsements")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<byte[]> exportEndorsements() throws IOException {
        byte[] csvData = exportService.exportEndorsements();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=endorsements.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<byte[]> exportAll() throws IOException {
        byte[] csvData = exportService.exportAll();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=office-knowledge-map.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
}
