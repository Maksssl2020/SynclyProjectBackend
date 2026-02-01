package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.authentication.*;
import com.synclyplatform.synclyprojectbackend.dto.two_factor_code.TwoFactorVerificationRequestDTO;
import com.synclyplatform.synclyprojectbackend.service.authentication.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(authenticationService.authenticate(authenticationRequest), HttpStatus.CREATED);
    }

    @PostMapping("/verify-turnstile")
    public ResponseEntity<TurnstileResponseDTO> verifyTurnstile(@RequestParam("token")  String token)  {
        return new ResponseEntity<>(authenticationService.verifyTurnstile(token), HttpStatus.OK);
    }

    @PostMapping("/android-app/login")
    public ResponseEntity<AndroidAppAuthenticationResponseDTO> androidLoginUser(@Valid @RequestBody AuthenticationRequestDTO authenticationRequest) throws Exception {
        return new ResponseEntity<>(authenticationService.authenticateAndroidUser(authenticationRequest), HttpStatus.CREATED);
    }

    @PostMapping("/exists/username")
    public ResponseEntity<Boolean> existsUsername(@RequestParam("username") String username) {
        return new ResponseEntity<>(authenticationService.usernameExists(username), HttpStatus.CREATED);
    }

    @PostMapping("/exists/email")
    public ResponseEntity<Boolean> existsEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(authenticationService.emailExists(email), HttpStatus.CREATED);
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<AuthenticationResponseDTO> verify2fa(
            @RequestBody @Valid TwoFactorVerificationRequestDTO twoFactorVerificationRequest,
            HttpServletRequest httpServletRequest
    ) throws Exception {
        return new ResponseEntity<>(authenticationService.validate2FARequest(twoFactorVerificationRequest, httpServletRequest), HttpStatus.OK);
    }

    @PutMapping("/change-password/{userId}")
    public ResponseEntity<HttpStatus> changePassword(@PathVariable Long userId, @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) throws Exception {
        authenticationService.changePassword(userId, changePasswordRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public AuthenticationResponseDTO login(@Payload AuthenticationRequestDTO authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }
}
