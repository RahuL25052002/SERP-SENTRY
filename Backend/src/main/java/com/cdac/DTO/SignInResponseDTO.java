package com.cdac.DTO;
import com.cdac.entities.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SignInResponseDTO {
	   private String email;
	    private UserRole role;
	    private String token;
	
}
