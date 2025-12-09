package com.smartinventory.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor 
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean active;

	@JsonIgnore
    private String password; 
    
    public UserResponseDto(Long id, String name, String email, String role, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    // Optional: If you want to also set password
    public UserResponseDto(Long id, String name, String email, String role, boolean active, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
        this.password = password;
    }

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

    

}
