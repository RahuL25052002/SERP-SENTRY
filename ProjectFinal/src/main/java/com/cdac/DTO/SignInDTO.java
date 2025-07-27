package com.cdac.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInDTO {
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	

}
