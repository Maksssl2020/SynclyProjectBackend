package com.synclyplatform.synclyprojectbackend.service.friend;

import com.synclyplatform.synclyprojectbackend.dto.friend.FriendDTO;
import com.synclyplatform.synclyprojectbackend.dto.friend.FriendUserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.model.friend.Friend;
import com.synclyplatform.synclyprojectbackend.model.friend.FriendStatus;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.FriendRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void sendFriendRequest(Long requesterId, Long receiverId) {
        User foundRequester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User foudReceiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (friendRepository.findByReceiverAndRequester(foudReceiver, foundRequester).isPresent()) {
            throw new IllegalStateException("Request already exists.");
        }

        Friend friend = Friend.builder()
                .requester(foundRequester)
                .receiver(foudReceiver)
                .status(FriendStatus.PENDING)
                .build();

        friendRepository.save(friend);
    }

    @Override
    public void acceptFriendRequest(Long requestId) {
        Friend foundRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        foundRequest.setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(foundRequest);
    }

    @Override
    public void declineFriendRequest(Long requestId) {
        Friend foundRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        foundRequest.setStatus(FriendStatus.DECLINED);
        friendRepository.save(foundRequest);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User foundFriend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Friend> relation = friendRepository.findByReceiverAndRequester(foundUser, foundFriend);
        relation.ifPresent(friendRepository::delete);
    }

    @Override
    public void blockUser(Long userId, Long blockedUserId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User foundBlockedUser = userRepository.findById(blockedUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Friend block = Friend.builder()
                .requester(foundUser)
                .receiver(foundBlockedUser)
                .status(FriendStatus.BLOCKED)
                .build();

        friendRepository.save(block);
    }

    @Override
    public List<FriendUserDTO> getFriendList(Long userId) {
        List<User> friendListNotDTO = getFriendListNotDTO(userId);
        Set<Long> friendListIds = friendListNotDTO.stream().map(User::getUserId).collect(Collectors.toSet());

        return friendListNotDTO.stream().map(friend -> {
                List<User> friendFriendsNotDTO = getFriendListNotDTO(friend.getUserId());
                Set<Long> friendFriendsIds = friendFriendsNotDTO.stream().map(User::getUserId).collect(Collectors.toSet());

                long mutualFriendsCount = friendFriendsIds.stream()
                        .filter(id -> friendListIds.contains(id) && !id.equals(friend.getUserId()))
                        .count();

                return FriendUserDTO.builder().user(userMapper.toDTO(friend)).mutualFriendsCount(mutualFriendsCount).build();
            })
        .collect(Collectors.toList());
    }


    @Override
    public List<FriendDTO> getPendingRequests(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return friendRepository.findByReceiverAndStatus(foundUser, FriendStatus.PENDING).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getSuggestedFriends(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<User> friendsOfUserFriends = getFriendListNotDTO(userId).stream()
                .flatMap(friend -> getFriendListNotDTO(friend.getUserId()).stream())
                .collect(Collectors.toSet());

        friendsOfUserFriends.remove(foundUser);

        Set<Long> alreadyConnectedIds = friendRepository.findAll().stream()
                .filter(friend -> (friend.getRequester().getUserId().equals(userId) || friend.getReceiver().getUserId().equals(userId)))
                .map(friend -> friend.getRequester().getUserId().equals(userId) ? friend.getReceiver().getUserId() : friend.getRequester().getUserId())
                .collect(Collectors.toSet());

        return friendsOfUserFriends.stream()
                .filter(friend -> !alreadyConnectedIds.contains(friend.getUserId()))
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<User> getFriendListNotDTO(Long userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Friend> friendsOfUser = friendRepository.findFriendsOfUser(foundUser);

        return friendsOfUser.stream()
                .map(friend -> friend.getRequester().getUserId().equals(foundUser.getUserId()) ? friend.getReceiver() : friend.getRequester())
                .collect(Collectors.toList());
    }

    public FriendDTO toDTO(Friend friend) {
        return FriendDTO.builder()
                .id(friend.getId())
                .receiver(userMapper.toDTO(friend.getReceiver()))
                .requester(userMapper.toDTO(friend.getRequester()))
                .status(friend.getStatus().toString())
                .createdAt(friend.getCreatedAt().toString())
                .build();
    }
}
