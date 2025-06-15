package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.authentication.AuthenticationRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.AuthenticationResponseDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.RegisterRequestDTO;
import com.synclyplatform.synclyprojectbackend.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequest) throws Exception {
        authenticationService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> loginUser(@Valid @RequestBody AuthenticationRequestDTO authenticationRequest) {
        return new ResponseEntity<>(authenticationService.authenticate(authenticationRequest),  HttpStatus.OK);
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public AuthenticationResponseDTO login(@Payload AuthenticationRequestDTO authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }
}
