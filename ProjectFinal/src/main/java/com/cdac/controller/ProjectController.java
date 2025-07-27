package com.cdac.controller;

import com.cdac.DTO.ProjectDTO;
import com.cdac.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}