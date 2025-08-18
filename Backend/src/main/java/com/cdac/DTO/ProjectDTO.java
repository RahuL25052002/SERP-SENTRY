package com.cdac.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjectDTO extends BaseDTO {
    
    @NotBlank(message = "Project name is required")
    private String name;
    
    @NotBlank(message = "Domain URL is required")
    private String domainUrl;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    // Username for display purposes (optional)
    private String userName;
}