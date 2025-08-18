package com.cdac.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeRankDTO {
	@NotBlank
	private String url;
}
