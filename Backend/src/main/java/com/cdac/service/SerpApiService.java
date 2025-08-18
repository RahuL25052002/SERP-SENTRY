package com.cdac.service;

import com.cdac.DTO.SerpApiResponseDTO;
import com.cdac.DTO.SerpResultDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SerpApiService {
    
    @Value("${serpapi.key:your-serpapi-key}")
    private String serpApiKey;
    
    @Value("${serpapi.base-url:https://serpapi.com/search}")
    private String serpApiBaseUrl;
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public SerpApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    public SerpApiResponseDTO searchKeywordRanking(String keyword, String domain, String location, String language) {
        try {
            // Build the SerpApi URL
            URI uri = UriComponentsBuilder.fromUriString(serpApiBaseUrl)
                    .queryParam("engine", "google")
                    .queryParam("q", keyword)
                    .queryParam("location", location != null ? location : "United States")
                    .queryParam("hl", language != null ? language : "en")
                    .queryParam("gl", "us")
                    .queryParam("api_key", serpApiKey)
                    .queryParam("num", "100") // Get up to 100 results
                    .build()
                    .toUri();
            
            log.info("Calling SerpApi with URL: {}", uri.toString().replace(serpApiKey, "***"));
            
            // Make API call
            String response = restTemplate.getForObject(uri, String.class);
            
            // Parse response
            return parseSerpApiResponse(response, keyword, domain, location);
            
        } catch (Exception e) {
            log.error("Error calling SerpApi for keyword: {} and domain: {}", keyword, domain, e);
            throw new RuntimeException("Failed to fetch ranking data from SerpApi: " + e.getMessage());
        }
    }
    
    private SerpApiResponseDTO parseSerpApiResponse(String jsonResponse, String keyword, String domain, String location) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            
            SerpApiResponseDTO responseDTO = new SerpApiResponseDTO();
            responseDTO.setSearchEngine("google");
            responseDTO.setKeyword(keyword);
            responseDTO.setLocation(location);
            
            // Parse organic results
            JsonNode organicResults = rootNode.get("organic_results");
            List<SerpResultDTO> results = new ArrayList<>();
            
            if (organicResults != null && organicResults.isArray()) {
                int position = 1;
                for (JsonNode result : organicResults) {
                    SerpResultDTO resultDTO = new SerpResultDTO();
                    resultDTO.setPosition(position);
                    resultDTO.setTitle(getTextValue(result, "title"));
                    resultDTO.setLink(getTextValue(result, "link"));
                    resultDTO.setSnippet(getTextValue(result, "snippet"));
                    resultDTO.setDisplayedLink(getTextValue(result, "displayed_link"));
                    
                    results.add(resultDTO);
                    position++;
                }
            }
            
            responseDTO.setOrganicResults(results);
            responseDTO.setTotalResults(results.size());
            
            return responseDTO;
            
        } catch (Exception e) {
            log.error("Error parsing SerpApi response", e);
            throw new RuntimeException("Failed to parse SerpApi response: " + e.getMessage());
        }
    }
    
    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return fieldNode != null ? fieldNode.asText() : null;
    }
    
    public Integer findDomainPosition(SerpApiResponseDTO serpResponse, String domain) {
        if (serpResponse.getOrganicResults() == null) {
            return null;
        }
        
        // Clean domain (remove protocol and www)
        String cleanDomain = cleanDomain(domain);
        
        for (SerpResultDTO result : serpResponse.getOrganicResults()) {
            if (result.getLink() != null) {
                String resultDomain = cleanDomain(result.getLink());
                if (resultDomain.contains(cleanDomain) || cleanDomain.contains(resultDomain)) {
                    return result.getPosition();
                }
            }
        }
        
        return null; // Not found in top results
    }
    
    private String cleanDomain(String url) {
        if (url == null) return "";
        
        return url.toLowerCase()
                .replaceAll("^https?://", "")
                .replaceAll("^www\\.", "")
                .replaceAll("/.*$", "")
                .trim();
    }
}