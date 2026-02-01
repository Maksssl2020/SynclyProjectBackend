package com.synclyplatform.synclyprojectbackend.service.login_history;

import com.synclyplatform.synclyprojectbackend.dto.authentication.AuthenticationResponseDTO;
import com.synclyplatform.synclyprojectbackend.dto.login_history.LoginHistoryDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoginHistoryService {

    void saveLoginHistory(Long userId, HttpServletRequest request);
    List<LoginHistoryDTO> findLoginHistoryByUserId(Long userId);
}
