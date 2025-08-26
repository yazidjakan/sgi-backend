package com.jakan.auth_service.entity.Security;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthenticationResponse {
    private String token;
    private List<String> roles;
    private Long userId;
}
