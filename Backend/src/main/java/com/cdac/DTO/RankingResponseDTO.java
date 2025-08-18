package com.cdac.DTO;

// DTO for ranking responses

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingResponseDTO extends BaseDTO {
    
    private Long projectId;
    private String projectName;
    private String domainUrl;
    private String keyword;
    private Integer position;
    private LocalDate dateChecked;
    private String searchEngine;
    private String location;
    private String urlFound;
    private String status; // "found", "not_found", "error"
}