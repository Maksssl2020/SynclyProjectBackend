package com.synclyplatform.synclyprojectbackend.service.authentication;

import com.synclyplatform.synclyprojectbackend.dto.authentication.AuthenticationRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.AuthenticationResponseDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.ChangePasswordRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.RegisterRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    void register(RegisterRequestDTO registerRequest) throws Exception;
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequest);
    void changePassword(Long userId, ChangePasswordRequestDTO changePasswordRequest) throws Exception;
    boolean usernameExists(String username);
    boolean emailExists(String email);
}
