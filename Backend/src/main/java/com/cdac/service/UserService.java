package com.cdac.service;

import java.util.Collection;

import org.springframework.http.ResponseEntity;

//import com.cdac.DTO.RecentUserProjectDTO;
import com.cdac.DTO.SignInDTO;
import com.cdac.DTO.SignInResponseDTO;
import com.cdac.DTO.SignUpDTO;
import com.cdac.DTO.UserDTO;

import jakarta.validation.Valid;

public interface UserService {
    String addUserDetails(SignUpDTO adddto);
    SignInResponseDTO authenticateUser(SignInDTO dto);
	Collection<UserDTO> getAllUsers();
	SignInResponseDTO signInIndividual(@Valid SignInDTO signInDTO);
	void createUser(@Valid SignUpDTO signUpDTO);
	SignInResponseDTO signInOrganization(@Valid SignInDTO signInDTO);
	UserDTO getUserById(Long id);
//	List<RecentUserProjectDTO> getRecentUsersWithProjects(LocalDate sinceDate);
	ResponseEntity<UserDTO> getUserByEmail(String email);
	
}