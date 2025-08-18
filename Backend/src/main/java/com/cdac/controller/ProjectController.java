package com.cdac.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.DTO.ProjectDTO;
import com.cdac.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/user/projects")
@Tag(name = "Project Management", description = "APIs for managing projects")
public class ProjectController {
  
    private final ProjectService projectService;

    @GetMapping
    @Operation(description = "Get all projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @Operation(description = "Get project by ID")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PostMapping
    @Operation(description = "Create a new project")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        return projectService.createProject(projectDTO);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update an existing project")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long id, 
            @Valid @RequestBody ProjectDTO projectDTO) {
        return projectService.updateProject(id, projectDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete a project")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        return projectService.deleteProject(id);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(description = "Get all projects for a specific user")
    public ResponseEntity<List<ProjectDTO>> getProjectsByUserId(@PathVariable Long userId) {
        return projectService.getProjectsByUserId(userId);
    }
}