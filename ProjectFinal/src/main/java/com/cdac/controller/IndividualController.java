package com.cdac.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.DTO.UserDTO;
import com.cdac.entities.UserRole;
import com.cdac.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/individuals")
public class IndividualController {
   
    private final UserService userService;
    
    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }
    
   @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getIndividualById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        if (user.getRole() == UserRole.ROLE_INDIVIDUAL) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}