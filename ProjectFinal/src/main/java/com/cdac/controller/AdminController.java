package com.cdac.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cdac.DTO.ApiResponse;
import com.cdac.service.AdminService;
import com.cdac.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {
    
    private final AdminService adminService;
    private final UserService userService;
    
    // Get all user detail
    @GetMapping("/users/{adminID}")  // ← Fixed: Added {adminID} to path
    @Operation(description = "Get all users details")
    public ResponseEntity<?> userDetails(@PathVariable @Valid Long adminID){
        try {
            System.out.println("in user controller method:get user details for admin");
            return ResponseEntity.ok(adminService.getAllUserDetails(adminID));
        }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage()));
        }
    }
   
    // Delete any user
    @DeleteMapping("/users/{userID}")
    @Operation(description = "Delete user details")
    public ResponseEntity<?> deleteUser(@PathVariable @Valid Long userID){
        System.out.println("in delete for the user");
        try {
            return ResponseEntity.ok
            (adminService.
                    deleteUser(userID));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage()));
        }
    }
}