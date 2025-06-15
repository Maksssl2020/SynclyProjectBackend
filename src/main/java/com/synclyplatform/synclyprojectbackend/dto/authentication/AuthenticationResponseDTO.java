package com.synclyplatform.synclyprojectbackend.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {

    private long userId;
    private String username;
    private String accessToken;
    private String role;
}
