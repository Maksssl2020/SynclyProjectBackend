package com.synclyplatform.synclyprojectbackend.service.user;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserPresenceServiceImpl implements UserPresenceService {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void setOnlineStatus(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setStatus(UserStatus.ONLINE);
            userRepository.save(user);

            notifyPresenceChange(user, true);

        });
    }

    @Override
    public void setOfflineStatus(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setStatus(UserStatus.OFFLINE);
            user.setLastActive(LocalDateTime.now());
            userRepository.save(user);

            notifyPresenceChange(user, false);
        });
    }

    @Override
    public boolean isUserOnline(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getStatus() == UserStatus.ONLINE)
                .orElse(false);
    }

    @Override
    public String getLastSeen(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getLastActive().toString())
                .orElse(null);
    }

    private void notifyPresenceChange(User user, boolean isOnline) {
        messagingTemplate.convertAndSend(
                "/topic/user/" + user.getUserId() + "/status",
                Map.of(
                        "userId", user.getUserId(),
                        "online", isOnline,
                        "lastSeen", user.getLastActive().toString()
                )
        );
    }
}
