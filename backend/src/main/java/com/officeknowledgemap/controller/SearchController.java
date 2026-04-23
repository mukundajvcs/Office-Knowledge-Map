package com.officeknowledgemap.controller;

import com.officeknowledgemap.dto.SearchRequest;
import com.officeknowledgemap.dto.SearchResultResponse;
import com.officeknowledgemap.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    
    private final SearchService searchService;
    
    @PostMapping
    public ResponseEntity<List<SearchResultResponse>> search(@RequestBody SearchRequest request) {
        return ResponseEntity.ok(searchService.search(request));
    }
}
