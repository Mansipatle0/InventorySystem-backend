package com.smartinventory.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor   // Generates no-args constructor automatically
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean active;

    @JsonIgnore
    private String password;

    // Constructor without password
    public UserResponseDto(Long id, String name, String email, String role, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    // Constructor with password
    public UserResponseDto(Long id, String name, String email, String role, boolean active, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
        this.password = password;
    }
}
