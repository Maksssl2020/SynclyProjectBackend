package com.synclyplatform.synclyprojectbackend.service.two_factor_code;

import org.springframework.stereotype.Service;

@Service
public interface TwoFactorCodeService {

    void generateTwoFactorCode(Long userId, String emailAddress, boolean authenticationAsAdmin) throws Exception;
    boolean verifyTwoFactorCode(Long userId, String code);
}
