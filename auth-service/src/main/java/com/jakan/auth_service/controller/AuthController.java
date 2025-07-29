package com.jakan.auth_service.controller;

import com.jakan.auth_service.dto.Security.RegisterDto;
import com.jakan.auth_service.entity.Security.AuthenticationRequest;
import com.jakan.auth_service.entity.Security.AuthenticationResponse;
import com.jakan.auth_service.service.Impl.Security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        System.out.println("Received authentication request for username: " + request.getUsername());
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authenticationService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
