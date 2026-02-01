package com.synclyplatform.synclyprojectbackend.dto.authentication;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AndroidAppAuthenticationResponseDTO {

    private long userId;
    private String username;
    private String role;
}
