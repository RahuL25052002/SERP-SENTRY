package com.cdac.dao;

import com.cdac.entities.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    
    // Find rankings by project ID
    List<Ranking> findByProjectIdOrderByDateCheckedDesc(Long projectId);
    
    // Find rankings by project and keyword
    List<Ranking> findByProjectIdAndKeywordOrderByDateCheckedDesc(Long projectId, String keyword);
    
    // Find latest ranking for a project and keyword
    Optional<Ranking> findFirstByProjectIdAndKeywordOrderByDateCheckedDesc(Long projectId, String keyword);
    
    // Find rankings by date range
    @Query("SELECT r FROM Ranking r WHERE r.project.id = :projectId AND r.dateChecked BETWEEN :startDate AND :endDate ORDER BY r.dateChecked DESC")
    List<Ranking> findByProjectIdAndDateRange(@Param("projectId") Long projectId, 
                                            @Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    // Find all rankings for a specific date
    List<Ranking> findByDateCheckedOrderByPositionAsc(LocalDate dateChecked);
    
    // Get ranking history for a keyword
    @Query("SELECT r FROM Ranking r WHERE r.project.id = :projectId AND r.keyword = :keyword ORDER BY r.dateChecked ASC")
    List<Ranking> getRankingHistory(@Param("projectId") Long projectId, @Param("keyword") String keyword);
    
    // Get average position for a keyword over time
    @Query("SELECT AVG(r.position) FROM Ranking r WHERE r.project.id = :projectId AND r.keyword = :keyword AND r.dateChecked BETWEEN :startDate AND :endDate")
    Double getAveragePosition(@Param("projectId") Long projectId, 
                            @Param("keyword") String keyword, 
                            @Param("startDate") LocalDate startDate, 
                            @Param("endDate") LocalDate endDate);
}