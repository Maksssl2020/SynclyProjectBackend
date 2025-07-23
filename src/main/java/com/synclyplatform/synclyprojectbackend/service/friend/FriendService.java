package com.synclyplatform.synclyprojectbackend.service.friend;

import com.synclyplatform.synclyprojectbackend.dto.friend.FriendDTO;
import com.synclyplatform.synclyprojectbackend.dto.friend.FriendUserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public interface FriendService {

    void sendFriendRequest(Long requesterId, Long receiverId);
    void acceptFriendRequest(Long requestId);
    void declineFriendRequest(Long requestId);
    void removeFriend(Long userId, Long friendId);
    void blockUser(Long userId, Long blockedUserId);
    List<FriendUserDTO> getFriendList(Long userId);
    List<FriendDTO> getPendingRequests(Long userId);
    List<UserDTO> getSuggestedFriends(Long userId);
}
