package com.cdac.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeRankResponseDTO {
	
	@NotNull
	 private Integer position;
	@NotBlank
	 private String searchEngine;
	@NotBlank
	 private String location;
	@NotBlank
	private String response;
}
