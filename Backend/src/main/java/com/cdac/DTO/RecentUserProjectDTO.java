package com.cdac.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecentUserProjectDTO {
    private Long userId;
    private String userName;
    private String email;
    private String projectName;
    private String domainUrl;
    private LocalDateTime updatedOn;
}
