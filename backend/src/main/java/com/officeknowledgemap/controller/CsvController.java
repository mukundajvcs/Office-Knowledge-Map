package com.officeknowledgemap.controller;

import com.officeknowledgemap.service.CsvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/csv")
@RequiredArgsConstructor
public class CsvController {
    
    private final CsvService csvService;
    
    @PostMapping("/upload/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadUsers(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "File is empty"));
        }
        
        if (!file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "File must be a CSV"));
        }
        
        return ResponseEntity.ok(csvService.bulkUploadUsers(file));
    }
}
