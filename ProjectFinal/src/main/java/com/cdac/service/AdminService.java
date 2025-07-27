package com.cdac.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.DTO.ApiResponse;
import com.cdac.DTO.UserDTO;

import lombok.AllArgsConstructor;
@Service
@Transactional
public
 interface AdminService  {
    List<UserDTO> getAllUserDetails(Long adminId);
    
    ApiResponse deleteUser(Long id);
} 