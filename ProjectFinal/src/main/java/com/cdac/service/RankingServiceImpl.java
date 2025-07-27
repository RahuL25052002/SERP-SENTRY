// Service Implementation
package com.cdac.service;

import com.cdac.DTO.*;
import com.cdac.dao.ProjectRepository;
import com.cdac.dao.RankingRepository;
import com.cdac.entities.Project;
import com.cdac.entities.Ranking;
import com.cdac.custom_exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class RankingServiceImpl implements RankingService {
    
    private final RankingRepository rankingRepository;
    private final ProjectRepository projectRepository;
    private final SerpApiService serpApiService;
    private final ModelMapper modelMapper;
    
    @Override
    public ResponseEntity<RankingResponseDTO> checkKeywordRanking(RankingRequestDTO requestDTO) {
        try {
            // Validate project exists
            Project project = projectRepository.findById(requestDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + requestDTO.getProjectId()));
            
            log.info("Checking ranking for keyword: {} for project: {}", requestDTO.getKeyword(), project.getName());
            
            // Call SerpApi to get search results
            SerpApiResponseDTO serpResponse = serpApiService.searchKeywordRanking(
                requestDTO.getKeyword(),
                project.getDomainUrl(),
                requestDTO.getLocation(),
                requestDTO.getLanguage()
            );
            
            // Find position of the domain in results
            Integer position = serpApiService.findDomainPosition(serpResponse, project.getDomainUrl());
            
            // Create ranking entity
            Ranking ranking = new Ranking();
            ranking.setProject(project);
            ranking.setKeyword(requestDTO.getKeyword());
            ranking.setPosition(position != null ? position : 0); // 0 means not found in top results
            ranking.setDateChecked(LocalDate.now());
            ranking.setSearchEngine(requestDTO.getSearchEngine());
            ranking.setLocation(requestDTO.getLocation());
            
            // Find the actual URL that ranked (if found)
            if (position != null && serpResponse.getOrganicResults() != null) {
                serpResponse.getOrganicResults().stream()
                    .filter(result -> result.getPosition().equals(position))
                    .findFirst()
                    .ifPresent(result -> ranking.setUrlFound(result.getLink()));
            }
            
            // Save ranking
            Ranking savedRanking = rankingRepository.save(ranking);
            
            // Convert to response DTO
            RankingResponseDTO responseDTO = convertToResponseDTO(savedRanking);
            responseDTO.setStatus(position != null ? "found" : "not_found");
            
            log.info("Ranking saved for keyword: {} with position: {}", requestDTO.getKeyword(), position);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error checking keyword ranking", e);
            throw new RuntimeException("Failed to check keyword ranking: " + e.getMessage());
        }
    }
    
    @Override
    public ResponseEntity<List<RankingResponseDTO>> getProjectRankings(Long projectId) {
        try {
            // Validate project exists
            projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
            
            List<Ranking> rankings = rankingRepository.findByProjectIdOrderByDateCheckedDesc(projectId);
            
            List<RankingResponseDTO> responseDTOs = rankings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(responseDTOs);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch project rankings: " + e.getMessage());
        }
    }
    
    @Override
    public ResponseEntity<List<RankingResponseDTO>> getKeywordHistory(Long projectId, String keyword) {
        try {
            // Validate project exists
            projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
            
            List<Ranking> rankings = rankingRepository.getRankingHistory(projectId, keyword);
            
            List<RankingResponseDTO> responseDTOs = rankings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(responseDTOs);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch keyword history: " + e.getMessage());
        }
    }
    
    @Override
    public ResponseEntity<List<RankingResponseDTO>> getRankingsByDateRange(Long projectId, LocalDate startDate, LocalDate endDate) {
        try {
            // Validate project exists
            projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
            
            List<Ranking> rankings = rankingRepository.findByProjectIdAndDateRange(projectId, startDate, endDate);
            
            List<RankingResponseDTO> responseDTOs = rankings.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(responseDTOs);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch rankings by date range: " + e.getMessage());
        }
    }
    
    @Override
    public ResponseEntity<String> deleteRanking(Long rankingId) {
        try {
            Ranking ranking = rankingRepository.findById(rankingId)
                .orElseThrow(() -> new ResourceNotFoundException("Ranking not found with id: " + rankingId));
            
            rankingRepository.delete(ranking);
            
            return ResponseEntity.ok("Ranking deleted successfully with id: " + rankingId);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete ranking: " + e.getMessage());
        }
    }
    
    private RankingResponseDTO convertToResponseDTO(Ranking ranking) {
        RankingResponseDTO dto = modelMapper.map(ranking, RankingResponseDTO.class);
        dto.setProjectId(ranking.getProject().getId());
        dto.setProjectName(ranking.getProject().getName());
        dto.setDomainUrl(ranking.getProject().getDomainUrl());
        return dto;
    }
}