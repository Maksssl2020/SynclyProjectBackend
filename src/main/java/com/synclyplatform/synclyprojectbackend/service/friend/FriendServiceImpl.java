package com.synclyplatform.synclyprojectbackend.service.friend;

import com.synclyplatform.synclyprojectbackend.dto.friend.FriendDTO;
import com.synclyplatform.synclyprojectbackend.dto.friend.FriendUserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.model.friend.Friend;
import com.synclyplatform.synclyprojectbackend.model.friend.FriendStatus;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.FriendRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
                .createdAt(LocalDateTime.now())
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
    public void removeRequest(Long requesterId, Long receiverId) {
        Friend foundRequest = friendRepository.findByRequesterUserIdAndReceiverUserId(requesterId, receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Friend request not found."));

        friendRepository.delete(foundRequest);
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
    public List<FriendDTO> getSentRequests(Long userId) {
        return friendRepository.findByRequesterUserIdAndStatus(userId, FriendStatus.PENDING).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getSuggestedFriends(Long userId) {
        final int LIMIT = 10;

        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Long> connectedIds = friendRepository.findConnectedUserIds(userId);
        connectedIds.add(userId);

        List<User> myFriends = getFriendListNotDTO(userId);

        Set<User> candidates = myFriends.stream()
                .flatMap(fr -> getFriendListNotDTO(fr.getUserId()).stream())
                .filter(u -> !connectedIds.contains(u.getUserId()))
                .collect(Collectors.toSet());

        if (candidates.size() < LIMIT) {
            Set<Long> excluded = new HashSet<>(connectedIds);
            candidates.forEach(u -> excluded.add(u.getUserId()));

            int missing = LIMIT - candidates.size();

            if (excluded.isEmpty()) excluded.add(-1L);

            List<User> randoms = userRepository.findRandomUsersExclude(userId, excluded, missing);
            candidates.addAll(randoms);
        }

        Set<Long> myFriendIds = myFriends.stream().map(User::getUserId).collect(Collectors.toSet());

        return candidates.stream()
                .limit(LIMIT)
                .map(candidate -> {
                    UserDTO dto = userMapper.toDTO(candidate);

                    Set<Long> candidateFriendIds = getFriendListNotDTO(candidate.getUserId()).stream()
                            .map(User::getUserId)
                            .collect(Collectors.toSet());

                    candidateFriendIds.retainAll(myFriendIds);
                    dto.setMutualFriendsCount(candidateFriendIds.size());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getUserFiendIds(Long userId) {
        return getFriendList(userId).stream()
                .map(friendUserDTO -> friendUserDTO.getUser().getUserId())
                .collect(Collectors.toList());
    }

    @Override
    public String getRequestStatus(Long requesterId, Long receiverId) {
        Optional<Friend> friendRequest = friendRepository.findByRequesterUserIdAndReceiverUserId(requesterId, receiverId);

        if (friendRequest.isPresent()) {
            return friendRequest.get().getStatus().toString();
        }

        return FriendStatus.NONE.toString();
    }

    @Override
    public Boolean checkIsFriend(User user, Long userId) {
        return friendRepository.existsByRequesterUserIdAndReceiverUserIdAndStatus(user.getUserId(), userId, FriendStatus.ACCEPTED);
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
                .status(friend.getStatus())
                .createdAt(friend.getCreatedAt().toString())
                .build();
    }
}
