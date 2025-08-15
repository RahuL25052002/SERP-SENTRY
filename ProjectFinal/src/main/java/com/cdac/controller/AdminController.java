package com.cdac.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.DTO.ApiResponse;
import com.cdac.DTO.RecentUserProjectDTO;
import com.cdac.DTO.UserCountResponse;
import com.cdac.DTO.UserRegistrationCountDTO;
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

    // Get all recent users based on updated activity on project
    @GetMapping("/users/recent-projects")
    public List<RecentUserProjectDTO> getRecentProjectsByUsers() {
        return adminService.getAllUsersWithRecentProjects();
    }

    // Get total current customers
    @GetMapping("/users/current-customers")
    public UserCountResponse getTotalUsersRegistered() {
        return adminService.getTotalUsersRegistered();
    }

    // Get user registrations per month
    @GetMapping("/users/registrations-per-month")
    public ResponseEntity<List<UserRegistrationCountDTO>> getUserRegistrationsPerMonth() {
        try {
            List<UserRegistrationCountDTO> result = adminService.getUserRegistrationsPerMonth();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all user details for a specific admin (adminID must be numeric)
    @GetMapping("/users/{adminID:\\d+}")
    @Operation(description = "Get all users details")
    public ResponseEntity<?> userDetails(@PathVariable @Valid Long adminID) {
        try {
            System.out.println("In user controller method: get user details for admin");
            return ResponseEntity.ok(adminService.getAllUserDetails(adminID));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage()));
        }
    }

    // Delete any user
    @DeleteMapping("/users/{userID:\\d+}")
    @Operation(description = "Delete user details")
    public ResponseEntity<?> deleteUser(@PathVariable @Valid Long userID) {
        System.out.println("In delete for the user");
        try {
            return ResponseEntity.ok(adminService.deleteUser(userID));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage()));
        }
    }
}
