package com.example.TestingJWT.services;


import com.example.TestingJWT.dtos.SignUpRequestDTO;
import com.example.TestingJWT.entities.User;
import com.example.TestingJWT.dtos.UserDto;
import com.example.TestingJWT.entities.enums.Role;
import com.example.TestingJWT.exceptions.ResourceNotFoundException;
import com.example.TestingJWT.exceptions.RuntimeConflictException;
import com.example.TestingJWT.repositories.UserRepository;
import com.example.TestingJWT.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RegularService regularService;


    public String[] login(String email, String password) {

        //use authentication manager to authenticate using email password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // get user from authentication
        User user = (User) authentication.getPrincipal();

        //generate both token for this user
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        String tokens[] = {accessToken, refreshToken};
        return tokens;
    }

    @Transactional
    public UserDto signup(SignUpRequestDTO signupDto) {

        //check user already signup or not
        User user = userRepository.findByEmail(signupDto.getEmail()).orElse(null);

        if(user != null)
            throw new RuntimeConflictException("Can Not Signup , User Already Exists with Email: "+signupDto.getEmail());


        //convert signup dto to User object
        User mappedUser = modelMapper.map(signupDto, User.class);

        //set roles -> by default we set role as RIDER
        mappedUser.setRoles(Set.of(Role.REGULAR));

        //bcrypt password
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));

        //save user
        User savedUser = userRepository.save(mappedUser);

        //1. create Regular User
        regularService.createRegularUser(savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    public String refreshToken(String refreshToken) {
        try {
            Long userId = jwtService.getUserIdFromToken(refreshToken);
            System.out.println("User ID from Refresh Token: " + userId);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

            System.out.println("User Found: " + user.getEmail());

            return jwtService.generateAccessToken(user);
        } catch (Exception e) {
            System.err.println("Error in Refresh Token Validation: " + e.getMessage());
            throw new AuthenticationServiceException("Invalid or Expired Refresh Token");
        }
    }


    public UserDto getCurrentUser(String token) {
        // Extract userId from the token
        Long userId = jwtService.getUserIdFromToken(token);

        // Fetch user from the repository
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Map to UserDto
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
