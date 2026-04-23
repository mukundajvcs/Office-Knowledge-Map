package com.officeknowledgemap.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EndorsementRequest {
    @NotNull
    private Long skillId;
    
    private String comment;
}
