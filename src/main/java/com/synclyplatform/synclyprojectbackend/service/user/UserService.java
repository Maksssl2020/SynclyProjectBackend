package com.synclyplatform.synclyprojectbackend.service.user;

import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserPresenceDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDTO findUserById(Long id);
    User disconnect(long userId);
    List<User> findConnectedUsers();
    List<UserDTO> searchUsers(String query);
    UserPresenceDTO getUserPresenceStatus(Long userId);
}
