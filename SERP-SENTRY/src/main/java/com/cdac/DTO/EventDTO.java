package com.cdac.DTO;

import java.time.LocalDate;

import com.cdac.entities.EventType;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter



public class EventDTO extends BaseDTO{
	
	
	@NotBlank
    private String title;
	@NotNull
    private LocalDate eventDate;
	@NotNull(message = "Event type is required")
    private EventType type;
	
	
   
}
