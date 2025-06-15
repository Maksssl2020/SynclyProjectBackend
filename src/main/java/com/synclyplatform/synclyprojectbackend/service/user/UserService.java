package com.synclyplatform.synclyprojectbackend.service.user;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User disconnect(long userId);
    List<User> findConnectedUsers();
}
