package com.cdac.DTO;

import java.time.LocalDateTime;

import com.cdac.entities.NotificationStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class NotificationDTO extends BaseDTO{
	
	@NotBlank
	private String message;

	@NotNull
    private LocalDateTime scheduledTime;

	@NotNull(message = "Notification type is required")
	private NotificationStatus status;
	
	
    
}
