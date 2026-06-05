package com.synclyplatform.synclyprojectbackend.dto.authentication;

import com.synclyplatform.synclyprojectbackend.model.image.Image;
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
    private Image avatar;
}
