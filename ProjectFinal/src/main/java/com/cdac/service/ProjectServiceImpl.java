package com.cdac.service;

import com.cdac.dao.ProjectRepository;
import com.cdac.dao.UserRepository;
import com.cdac.entities.Project;
import com.cdac.entities.User;
import com.cdac.DTO.ProjectDTO;
import com.cdac.custom_exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            List<ProjectDTO> projectDTOs = projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(projectDTOs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch projects: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ProjectDTO> getProjectById(Long id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        
        ProjectDTO projectDTO = convertToDTO(project);
        return ResponseEntity.ok(projectDTO);
    }

    @Override
    public ResponseEntity<ProjectDTO> createProject(ProjectDTO projectDTO) {
        try {
            // Validate user exists
            User user = userRepository.findById(projectDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + projectDTO.getUserId()));
            
            // Convert DTO to Entity
            Project project = convertToEntity(projectDTO);
            project.setUser(user);
            
            // Save project
            Project savedProject = projectRepository.save(project);
            
            // Convert back to DTO and return
            ProjectDTO savedProjectDTO = convertToDTO(savedProject);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProjectDTO);
            
        } catch (ResourceNotFoundException e) {
            throw e; // Let global exception handler deal with it
        } catch (Exception e) {
            throw new RuntimeException("Failed to create project: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ProjectDTO> updateProject(Long id, ProjectDTO projectDTO) {
        try {
            // Find existing project
            Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
            
            // Validate user exists if userId is being changed
            if (!existingProject.getUser().getId().equals(projectDTO.getUserId())) {
                User user = userRepository.findById(projectDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + projectDTO.getUserId()));
                existingProject.setUser(user);
            }
            
            // Update project fields
            existingProject.setName(projectDTO.getName());
            existingProject.setDomainUrl(projectDTO.getDomainUrl());
            
            // Save updated project
            Project updatedProject = projectRepository.save(existingProject);
            
            // Convert to DTO and return
            ProjectDTO updatedProjectDTO = convertToDTO(updatedProject);
            return ResponseEntity.ok(updatedProjectDTO);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update project: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> deleteProject(Long id) {
        try {
            // Check if project exists
            Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
            
            // Delete project
            projectRepository.delete(project);
            
            return ResponseEntity.ok("Project deleted successfully with id: " + id);
            
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete project: " + e.getMessage());
        }
    }

    // Helper method to convert Project entity to ProjectDTO
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = modelMapper.map(project, ProjectDTO.class);
        dto.setUserId(project.getUser().getId());
        dto.setUserName(project.getUser().getUserName()); // Optional: for display
        return dto;
    }

    // Helper method to convert ProjectDTO to Project entity (without user mapping)
    private Project convertToEntity(ProjectDTO dto) {
        Project project = modelMapper.map(dto, Project.class);
        // Note: User is set separately in the service methods
        return project;
    }
}