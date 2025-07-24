package com.cdac.DTO;

import java.time.LocalDate;

import com.cdac.entities.UserRole;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UserDTO extends BaseDTO{
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private LocalDate dob;
	
	private UserRole role;
}
