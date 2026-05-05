package com.synclyplatform.synclyprojectbackend.service.user;

import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserPresenceDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDTO findUserById(Long id);
    User disconnect(long userId);
    List<User> findConnectedUsers();
    List<UserDTO> searchUsers(String query);
    UserPresenceDTO getUserPresenceStatus(Long userId);

    Page<UserDTO> getAllUsers(int page, int size, UserRole userRole, UserStatus userStatus, String searchQuery, TimestampSortOption sortOption);
}
