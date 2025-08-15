package com.cdac.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.DTO.ApiResponse;
import com.cdac.DTO.RecentUserProjectDTO;
import com.cdac.DTO.UserCountResponse;
import com.cdac.DTO.UserDTO;
import com.cdac.DTO.UserRegistrationCountDTO;
@Service
@Transactional
public
 interface AdminService  {
    List<UserDTO> getAllUserDetails(Long adminId);
    
    ApiResponse deleteUser(Long id);
    
    List<RecentUserProjectDTO> getAllUsersWithRecentProjects();
    
    UserCountResponse getTotalUsersRegistered();
    List<UserRegistrationCountDTO> getUserRegistrationsPerMonth() ;
} 