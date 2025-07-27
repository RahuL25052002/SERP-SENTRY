package com.cdac.controller;

import com.cdac.DTO.RankingRequestDTO;
import com.cdac.DTO.RankingResponseDTO;
import com.cdac.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/rankings")
@Tag(name = "Ranking Management", description = "APIs for managing keyword rankings using SerpApi")
public class RankingController {
    
    private final RankingService rankingService;
    
    @PostMapping("/check")
    @Operation(description = "Check keyword ranking for a project using SerpApi")
    public ResponseEntity<RankingResponseDTO> checkKeywordRanking(@Valid @RequestBody RankingRequestDTO requestDTO) {
        return rankingService.checkKeywordRanking(requestDTO);
    }
    
    @GetMapping("/project/{projectId}")
    @Operation(description = "Get all rankings for a specific project")
    public ResponseEntity<List<RankingResponseDTO>> getProjectRankings(@PathVariable Long projectId) {
        return rankingService.getProjectRankings(projectId);
    }
    
    @GetMapping("/project/{projectId}/keyword/{keyword}/history")
    @Operation(description = "Get ranking history for a specific keyword")
    public ResponseEntity<List<RankingResponseDTO>> getKeywordHistory(
            @PathVariable Long projectId, 
            @PathVariable String keyword) {
        return rankingService.getKeywordHistory(projectId, keyword);
    }
    
    @GetMapping("/project/{projectId}/date-range")
    @Operation(description = "Get rankings by date range")
    public ResponseEntity<List<RankingResponseDTO>> getRankingsByDateRange(
            @PathVariable Long projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return rankingService.getRankingsByDateRange(projectId, startDate, endDate);
    }
    
    @DeleteMapping("/{rankingId}")
    @Operation(description = "Delete a ranking record")
    public ResponseEntity<String> deleteRanking(@PathVariable Long rankingId) {
        return rankingService.deleteRanking(rankingId);
    }
    
    // Bulk ranking check endpoint
    @PostMapping("/bulk-check")
    @Operation(description = "Check multiple keywords for a project")
    public ResponseEntity<List<RankingResponseDTO>> bulkCheckRankings(@Valid @RequestBody List<RankingRequestDTO> requestDTOs) {
        // This would process multiple keywords at once
        return ResponseEntity.ok(
            requestDTOs.stream()
                .map(requestDTO -> rankingService.checkKeywordRanking(requestDTO).getBody())
                .collect(java.util.stream.Collectors.toList())
        );
    }
}