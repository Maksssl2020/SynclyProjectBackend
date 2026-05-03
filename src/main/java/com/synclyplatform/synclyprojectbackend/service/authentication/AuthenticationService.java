package com.synclyplatform.synclyprojectbackend.service.authentication;

import com.synclyplatform.synclyprojectbackend.dto.authentication.*;
import com.synclyplatform.synclyprojectbackend.dto.two_factor_code.TwoFactorVerificationRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    void register(RegisterRequestDTO registerRequest) throws Exception;
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequest) throws Exception;
    AndroidAppAuthenticationResponseDTO authenticateAndroidUser(AuthenticationRequestDTO authenticationRequest) throws Exception;
    void changePassword(Long userId, ChangePasswordRequestDTO changePasswordRequest) throws Exception;
    boolean usernameExists(String username);
    boolean emailExists(String email);

    TurnstileResponseDTO verifyTurnstile(String token);

    AuthenticationResponseDTO validate2FARequest(@Valid TwoFactorVerificationRequestDTO twoFactorVerificationRequest, HttpServletRequest httpServletRequest) throws Exception;
}
