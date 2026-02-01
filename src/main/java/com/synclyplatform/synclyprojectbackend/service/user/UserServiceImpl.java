package com.synclyplatform.synclyprojectbackend.service.user;

import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserPresenceDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO findUserById(Long id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return userMapper.toDTO(foundUser);
    }

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

    @Override
    public List<UserDTO> searchUsers(String query) {
        return userRepository.searchUserByQuery(query).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserPresenceDTO getUserPresenceStatus(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    UserStatus userStatus = user.getStatus();
                    LocalDateTime lastSeen = user.getLastActive();
                    return UserPresenceDTO.builder()
                            .userId(userId)
                            .online(userStatus == UserStatus.ONLINE)
                            .lastSeen(lastSeen.toString())
                            .build();
                })
                .orElse(null);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}
