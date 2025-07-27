package com.cdac.service;

import com.cdac.DTO.RankingRequestDTO;
import com.cdac.DTO.RankingResponseDTO;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;

// Service Interface
public interface RankingService {
    ResponseEntity<RankingResponseDTO> checkKeywordRanking(RankingRequestDTO requestDTO);
    ResponseEntity<List<RankingResponseDTO>> getProjectRankings(Long projectId);
    ResponseEntity<List<RankingResponseDTO>> getKeywordHistory(Long projectId, String keyword);
    ResponseEntity<List<RankingResponseDTO>> getRankingsByDateRange(Long projectId, LocalDate startDate, LocalDate endDate);
    ResponseEntity<String> deleteRanking(Long rankingId);
}