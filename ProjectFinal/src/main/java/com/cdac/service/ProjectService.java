package com.cdac.service;

import com.cdac.DTO.ProjectDTO;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface ProjectService {
    
    ResponseEntity<List<ProjectDTO>> getAllProjects();
    
    ResponseEntity<ProjectDTO> getProjectById(Long id);
    
    ResponseEntity<ProjectDTO> createProject(ProjectDTO projectDTO);
    
    ResponseEntity<ProjectDTO> updateProject(Long id, ProjectDTO projectDTO);
    
    ResponseEntity<String> deleteProject(Long id);
}