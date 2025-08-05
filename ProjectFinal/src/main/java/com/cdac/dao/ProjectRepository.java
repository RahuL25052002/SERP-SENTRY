package com.cdac.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cdac.entities.Project;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Add custom query methods for Project if needed
    List<Project> findByUserId(Long userId);
    //pagination-->
    
//    List<Project> findByUserId(Long userId);
    List<Project> findByUser_Id(Long userId); 

} 