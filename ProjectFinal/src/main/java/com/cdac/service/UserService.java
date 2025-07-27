package com.cdac.service;

import com.cdac.DTO.SignUpDTO;
import com.cdac.DTO.UserDTO;

import jakarta.validation.Valid;

import java.util.Collection;

import com.cdac.DTO.SignInDTO;
import com.cdac.DTO.SignInResponseDTO;

public interface UserService {
    String addUserDetails(SignUpDTO adddto);
    SignInResponseDTO authenticateUser(SignInDTO dto);
	Collection<UserDTO> getAllUsers();
	SignInResponseDTO signInIndividual(@Valid SignInDTO signInDTO);
	void createUser(@Valid SignUpDTO signUpDTO);
	SignInResponseDTO signInOrganization(@Valid SignInDTO signInDTO);
	UserDTO getUserById(Long id);
}