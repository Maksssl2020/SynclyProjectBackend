package com.synclyplatform.synclyprojectbackend.service.admin;

import com.synclyplatform.synclyprojectbackend.dto.admin.AdminPanelStatisticsDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UpdateUserDataAsAdminRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    AdminPanelStatisticsDTO getAdminStatistics();
    void updateUserAsAdmin(Long userId, UpdateUserDataAsAdminRequestDTO updateUserDataAsAdminRequestDTO);
}
