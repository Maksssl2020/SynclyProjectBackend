package com.synclyplatform.synclyprojectbackend.dto.login_history;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistoryDTO {

    private Long id;
    private Long userId;
    private String ip;
    private String city;
    private String country;
    private String userAgent;
    private String timestamp;
}
