package com.cdac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cdac.DTO.SignInDTO;
import com.cdac.DTO.SignInResponseDTO;
import com.cdac.DTO.SignUpDTO;
import com.cdac.entities.UserRole;
import com.cdac.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        if (signUpDTO.getRole() != UserRole.ROLE_ADMIN &&
            signUpDTO.getRole() != UserRole.ROLE_INDIVIDUAL) {
            return ResponseEntity.badRequest().body("Invalid role. Must be ROLE_ADMIN or ROLE_INDIVIDUAL.");
        }

        userService.addUserDetails(signUpDTO);
        return ResponseEntity.ok(signUpDTO.getRole() + " registered successfully.");
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInResponseDTO> signIn(@Valid @RequestBody SignInDTO signInDTO) {
        SignInResponseDTO response = userService.authenticateUser(signInDTO);
        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        if (response.getRole() != UserRole.ROLE_ADMIN &&
            response.getRole() != UserRole.ROLE_INDIVIDUAL) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(response);
    }
}
