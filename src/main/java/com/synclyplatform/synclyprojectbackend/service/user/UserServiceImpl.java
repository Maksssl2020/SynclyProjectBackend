package com.synclyplatform.synclyprojectbackend.service.user;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User disconnect(long userId) {
        User foundUser = userRepository.findById(userId)
                .orElse(null);

        if (foundUser != null) {
            foundUser.setStatus(UserStatus.OFFLINE);
            userRepository.save(foundUser);
            return foundUser;
        }

        return null;
    }

    @Override
    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(UserStatus.ONLINE);
    }
}
