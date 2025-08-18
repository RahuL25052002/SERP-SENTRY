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
@Slf4j// for logging purpose we have used this annotation. 
public class RankingServiceImpl implements RankingService {
    
    private final RankingRepository rankingRepository;
    private final ProjectRepository projectRepository;
    private final SerpApiService serpApiService;
    private final ModelMapper modelMapper;
    
    @Override
    public ResponseEntity<RankingResponseDTO> checkKeywordRanking(RankingRequestDTO requestDTO) {
        try {
            boolean isFreeTrial = requestDTO.getProjectId() == 0;

            Project project = null;
            String domainUrl;

            if (isFreeTrial) {
                // Free trial: use domain from frontend
                domainUrl = requestDTO.getDomainUrl();
                log.info("Free trial rank check for domain: {}, keyword: {}", domainUrl, requestDTO.getKeyword());
            } else {
                // Authenticated flow: fetch project
                project = projectRepository.findById(requestDTO.getProjectId())
                        .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + requestDTO.getProjectId()));
                domainUrl = project.getDomainUrl();
                log.info("Authenticated rank check for project: {}, keyword: {}", project.getName(), requestDTO.getKeyword());
            }

            // Call SerpApi to get search results
            SerpApiResponseDTO serpResponse = serpApiService.searchKeywordRanking(
                    requestDTO.getKeyword(),
                    domainUrl,
                    requestDTO.getLocation(),
                    requestDTO.getLanguage()
            );

            // Find position of the domain in results
            Integer position = serpApiService.findDomainPosition(serpResponse, domainUrl);

            RankingResponseDTO responseDTO = new RankingResponseDTO();
            responseDTO.setKeyword(requestDTO.getKeyword());
            responseDTO.setPosition(position != null ? position : 0);
            responseDTO.setSearchEngine(requestDTO.getSearchEngine());
            responseDTO.setLocation(requestDTO.getLocation());
            responseDTO.setDateChecked(LocalDate.now());
            responseDTO.setDomainUrl(domainUrl);
            responseDTO.setStatus(position != null ? "found" : "not_found");

            if (!isFreeTrial) {
                // Save to DB only for logged-in users
                Ranking ranking = new Ranking();
                ranking.setProject(project);
                ranking.setKeyword(requestDTO.getKeyword());
                ranking.setPosition(responseDTO.getPosition());
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

                Ranking savedRanking = rankingRepository.save(ranking);
                responseDTO.setProjectId(project.getId());
                responseDTO.setProjectName(project.getName());

                log.info("Ranking saved for keyword: {} with position: {}", requestDTO.getKeyword(), position);
            }

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

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