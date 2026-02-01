package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.login_history.LoginHistoryDTO;
import com.synclyplatform.synclyprojectbackend.model.login_history.LoginHistory;
import org.springframework.stereotype.Component;

@Component
public class LoginHistoryMapper {

    public LoginHistoryDTO loginHistoryToLoginHistoryDTO(LoginHistory loginHistory) {
        return LoginHistoryDTO.builder()
                .id(loginHistory.getId())
                .ip(loginHistory.getIp())
                .userId(loginHistory.getUserId())
                .city(loginHistory.getCity())
                .country(loginHistory.getCountry())
                .timestamp(loginHistory.getTimestamp().toString())
                .userAgent(loginHistory.getUserAgent())
                .build();
    }
}
