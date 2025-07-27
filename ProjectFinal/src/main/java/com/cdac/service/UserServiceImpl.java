package com.cdac.service;

import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.DTO.SignInDTO;
import com.cdac.DTO.SignInResponseDTO;
import com.cdac.DTO.SignUpDTO;
import com.cdac.DTO.UserDTO;
import com.cdac.custom_exceptions.ApiException;
import com.cdac.custom_exceptions.AuthenticationException;
import com.cdac.dao.UserRepository;
import com.cdac.entities.User;
import com.cdac.entities.UserRole;
import com.cdac.security.JwtUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public String addUserDetails(SignUpDTO adddto) {
        String email = adddto.getEmail() != null ? adddto.getEmail().toLowerCase() : "";

        // Enforce admin restriction
        if (adddto.getRole() == UserRole.ROLE_ADMIN && !email.equals("admin@gmail.com")) {
            throw new ApiException("Only admin can register as admin.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ApiException("Duplicate email id");
        }

        // Debug: Print the DTO values before mapping
        System.out.println("SignUpDTO - userName: " + adddto.getUserName());
        System.out.println("SignUpDTO - email: " + adddto.getEmail());
        System.out.println("SignUpDTO - role: " + adddto.getRole());

        // Use ModelMapper to map SignUpDTO to User
        User user = modelMapper.map(adddto, User.class);
        
        // Override email with lowercase version
        user.setEmail(email);
        
        // Encode password (ModelMapper maps the plain password, we need to encode it)
        user.setPassword(passwordEncoder.encode(adddto.getPassword()));
        
        // Debug: Print the mapped User values
        System.out.println("Mapped User - userName: " + user.getUsername());
        System.out.println("Mapped User - email: " + user.getEmail());
        System.out.println("Mapped User - role: " + user.getRole());
        
        userRepository.save(user);

        if (adddto.getRole() == UserRole.ROLE_ADMIN) {
            return "Admin registered successfully.";
        } else {
            return "User registered successfully.";
        }
    }

    public SignInResponseDTO authenticateUser(SignInDTO dto) {
        User entity = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new AuthenticationException("Invalid email"));

        if (!passwordEncoder.matches(dto.getPassword(), entity.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        String token = jwtUtils.generateJwtToken(authentication);
        User userDetails = (User) authentication.getPrincipal();

        SignInResponseDTO response = new SignInResponseDTO();
        response.setEmail(userDetails.getEmail());
        response.setRole(userDetails.getRole());
        response.setToken(token);
        return response;
    }

    @Override
    public Collection<UserDTO> getAllUsers() {
        return userRepository.findByRole(UserRole.ROLE_INDIVIDUAL)
            .stream()
            .map(user -> {
                // Debug: Print user values before mapping
                System.out.println("User from DB - userName: " + user.getUsername());
                System.out.println("User from DB - email: " + user.getEmail());
                
                // Use ModelMapper to map User to UserDTO
                UserDTO dto = modelMapper.map(user, UserDTO.class);
                
                // Debug: Print mapped DTO values
                System.out.println("Mapped DTO - userName: " + dto.getUserName());
                System.out.println("Mapped DTO - email: " + dto.getEmail());
                
                return dto;
            })
            .toList();
    }

    @Override
    public SignInResponseDTO signInIndividual(@Valid SignInDTO signInDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createUser(@Valid SignUpDTO signUpDTO) {
        // TODO Auto-generated method stub
    }

    @Override
    public SignInResponseDTO signInOrganization(@Valid SignInDTO signInDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserDTO getUserById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }
}