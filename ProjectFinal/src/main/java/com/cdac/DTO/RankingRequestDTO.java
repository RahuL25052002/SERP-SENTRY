package com.cdac.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

// DTO for creating/updating rankings
@Getter
@Setter
public class RankingRequestDTO {
    
    @NotNull(message = "Project ID is required")
    private Long projectId;
    
    @NotBlank(message = "Keyword is required")
    private String keyword;
    
    @NotBlank(message = "Search engine is required")
    private String searchEngine; // Default: "google"
    
    private String location; // Default: "United States"
    
    private String language; // Default: "en"
}