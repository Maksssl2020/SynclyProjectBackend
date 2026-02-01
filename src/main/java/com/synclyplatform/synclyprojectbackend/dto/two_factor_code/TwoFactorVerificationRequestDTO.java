package com.synclyplatform.synclyprojectbackend.dto.two_factor_code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorVerificationRequestDTO {
    private Long userId;
    private String code;
}

