package com.synclyplatform.synclyprojectbackend.service.admin;

import com.synclyplatform.synclyprojectbackend.dto.admin.AdminPanelStatisticsDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UpdateUserDataAsAdminRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    AdminPanelStatisticsDTO getAdminStatistics();
    void updateUserAsAdmin(Long userId, User adminUser, UpdateUserDataAsAdminRequestDTO updateUserDataAsAdminRequestDTO);
    void blockUserById(User adminUser, Long userId);
    void unblockUserById(User adminUser, Long userId);

}
