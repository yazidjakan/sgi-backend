package com.jakan.user_service.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserCreateDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;
}