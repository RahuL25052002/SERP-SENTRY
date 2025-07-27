package com.cdac.DTO;

import java.time.LocalDate;

import com.cdac.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserDTO extends BaseDTO{
	
	@NotBlank
    private String firstName;
	@NotBlank
	private String lastName;
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	@NotNull
	private LocalDate dob;
	@NotNull
	private UserRole role;

}
