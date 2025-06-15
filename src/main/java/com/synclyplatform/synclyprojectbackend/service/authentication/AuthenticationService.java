package com.synclyplatform.synclyprojectbackend.service.authentication;

import com.synclyplatform.synclyprojectbackend.dto.authentication.AuthenticationRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.AuthenticationResponseDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.RegisterRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    void register(RegisterRequestDTO registerRequest) throws Exception;
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequest);
}
