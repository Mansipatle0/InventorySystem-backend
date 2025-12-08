package com.smartinventory.backend.service.impl;

import com.smartinventory.backend.common.exception.NotFoundException ;
import com.smartinventory.backend.dto.UserRequestDto;
import com.smartinventory.backend.dto.UserResponseDto;
import com.smartinventory.backend.entity.User;
import com.smartinventory.backend.repository.UserRepository;
import com.smartinventory.backend.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<UserResponseDto> findAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto create(UserRequestDto req) {
        User u = new User();

        apply(req, u);

        // ⭐ Hash password before saving
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        } else {
            u.setPassword(passwordEncoder.encode("123456")); // default password
        }

        User saved = repo.save(u);
        return toDto(saved);
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto req) {
        User u = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        apply(req, u);

        // ⭐ Update password only if provided
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        User saved = repo.save(u);
        return toDto(saved);
    }

    @Override
    public UserResponseDto toggleActive(Long id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));

        u.setActive(!u.isActive());
        User saved = repo.save(u);
        return toDto(saved);
    }

    @Override
    public UserResponseDto getById(Long id) {
        User u = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
        return toDto(u);
    }

    // ----------- Apply request to entity (without password) -----------
    private void apply(UserRequestDto req, User u) {

        if (req.getName() != null)
            u.setName(req.getName());

        if (req.getEmail() != null)
            u.setEmail(req.getEmail());

        if (req.getRole() != null)
            u.setRole(User.Role.valueOf(req.getRole()));

        u.setActive(req.isActive());
    }

    // ----------- Convert Entity → DTO -----------
    private UserResponseDto toDto(User u) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole().name());
        dto.setActive(u.isActive());
        dto.setPassword(u.getPassword()); 
        return dto;
    }
}
