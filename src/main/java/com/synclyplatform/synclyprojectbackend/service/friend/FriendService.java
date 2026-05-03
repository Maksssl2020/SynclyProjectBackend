package com.synclyplatform.synclyprojectbackend.service.friend;

import com.synclyplatform.synclyprojectbackend.dto.friend.FriendDTO;
import com.synclyplatform.synclyprojectbackend.dto.friend.FriendUserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
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
    void removeRequest(Long requesterId, Long receiverId);

    List<FriendUserDTO> getFriendList(Long userId);
    List<FriendDTO> getPendingRequests(Long userId);
    List<FriendDTO> getSentRequests(Long userId);
    List<UserDTO> getSuggestedFriends(Long userId);
    List<Long> getUserFiendIds(Long userId);
    String getRequestStatus(Long requesterId, Long receiverId);

    Boolean checkIsFriend(User user, Long userId);
}
