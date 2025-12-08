package com.smartinventory.backend.controller;


import com.smartinventory.backend.dto.UserRequestDto;
import com.smartinventory.backend.dto.UserResponseDto;
import com.smartinventory.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:4200")  // change if needed
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    // 🔹 LIST ALL USERS  -> GET /api/admin/users
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> list() {
        return ResponseEntity.ok(userService.findAll());
    }

    // 🔹 GET ONE USER BY ID (optional but useful) -> GET /api/admin/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // 🔹 CREATE USER -> POST /api/admin/users
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.create(request));
    }

    // 🔹 UPDATE USER -> PUT /api/admin/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long id,
            @RequestBody UserRequestDto request
    ) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    // 🔹 ENABLE / DISABLE USER -> PATCH /api/admin/users/{id}/toggle-active
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<UserResponseDto> toggleActive(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleActive(id));
    }
}
