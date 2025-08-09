package com.synclyplatform.synclyprojectbackend.service.user;

import org.springframework.stereotype.Service;

@Service
public interface UserPresenceService {

    void setOnlineStatus(String username);
    void setOfflineStatus(String username);
    boolean isUserOnline(String username);
    String getLastSeen(String username);
}
