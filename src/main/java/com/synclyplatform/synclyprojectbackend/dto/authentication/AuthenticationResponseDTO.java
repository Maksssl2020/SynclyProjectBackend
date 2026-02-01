package com.synclyplatform.synclyprojectbackend.dto.authentication;

import com.synclyplatform.synclyprojectbackend.model.image.Image;
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
    private String email;
    private String accessToken;
    private String role;
    private Image profileImage;
    private boolean requiresTwoFactorAuthentication;
}
