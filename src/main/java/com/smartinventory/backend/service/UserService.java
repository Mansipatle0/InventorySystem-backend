package com.smartinventory.backend.service;



import java.util.List;

import com.smartinventory.backend.dto.UserRequestDto;
import com.smartinventory.backend.dto.UserResponseDto;

public interface UserService {

    List<UserResponseDto> findAll();

    UserResponseDto create(UserRequestDto request);

    UserResponseDto update(Long id, UserRequestDto request);

    UserResponseDto toggleActive(Long id);

    UserResponseDto getById(Long id);
}
