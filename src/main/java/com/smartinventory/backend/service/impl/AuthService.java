package com.smartinventory.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired; 

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.smartinventory.backend.dto.LoginRequest;
import com.smartinventory.backend.dto.UserResponseDto;
import com.smartinventory.backend.entity.User;
import com.smartinventory.backend.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDto login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            throw new RuntimeException("Invalid email");
        }

        if (!user.isActive()) {
            throw new RuntimeException("User disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        // Note: Password is NOT returned for security
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().toString(),
                user.isActive()
        );
    }
}


