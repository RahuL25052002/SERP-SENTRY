package com.cdac.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.DTO.ApiResponse;
import com.cdac.DTO.RecentUserProjectDTO;
import com.cdac.DTO.UserDTO;
import com.cdac.custom_exceptions.ResourceNotFoundException;
import com.cdac.dao.AdminRepository;
import com.cdac.entities.User;
import com.cdac.entities.UserRole;
import com.cdac.security.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@Transactional
//only over the methods
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public List<UserDTO> getAllUserDetails(Long adminId) {
        User admin = adminRepository.findById(adminId)
            .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + adminId));
        if (admin.getRole() != UserRole.ROLE_ADMIN) {
            throw new RuntimeException("User is not an admin");
        }

        //List<User> users = adminRepository.findByRole();
        
        List<UserRole> roles = Arrays.asList(UserRole.ROLE_ORGANIZATION, UserRole.ROLE_INDIVIDUAL);
        List<User> users = adminRepository.findByRoleIn(roles);

        return users.stream()
            .map(user -> {
                // Use ModelMapper for automatic mapping
                UserDTO dto = modelMapper.map(user, UserDTO.class);
                
                // Debug: Print values to verify ModelMapper is working
                System.out.println("ModelMapper - User: " + user.getUserName() + " -> DTO: " + dto.getUserName());
                
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public ApiResponse deleteUser(Long userID) {
        User userEntity = adminRepository.findById(userID)
            .orElseThrow(() -> new ResourceNotFoundException("Invalid user id !!!!!"));
        adminRepository.delete(userEntity);

        return new ApiResponse("User deleted!!");
    }
    @Override
    public List<RecentUserProjectDTO> getAllUsersWithRecentProjects() {
        return adminRepository.findAllUsersWithRecentProjects();
    }

    
    
}