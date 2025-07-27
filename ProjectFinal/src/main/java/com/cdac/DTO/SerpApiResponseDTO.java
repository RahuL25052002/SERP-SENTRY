package com.cdac.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SerpApiResponseDTO {
    private String searchEngine;
    private String keyword;
    private String location;
    private Integer totalResults;
    private java.util.List<SerpResultDTO> organicResults;
}