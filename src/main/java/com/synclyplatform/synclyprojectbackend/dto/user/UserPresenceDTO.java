package com.synclyplatform.synclyprojectbackend.dto.user;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPresenceDTO {

    private Long userId;
    private boolean online;
    private String lastSeen;
}
