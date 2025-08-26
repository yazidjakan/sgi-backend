package com.jakan.auth_service.dto;


import java.util.Set;

public record UserDto(
        Long id,
        String username,
        String email,
        String password,
        Set<RoleDto> roleDtos
) {
}
