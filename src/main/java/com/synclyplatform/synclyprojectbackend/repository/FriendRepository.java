package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.friend.Friend;
import com.synclyplatform.synclyprojectbackend.model.friend.FriendStatus;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByReceiverAndStatus(User receiver, FriendStatus status);
    List<Friend> findByRequesterUserIdAndStatus(Long requesterId, FriendStatus status);

    @Query("""
        SELECT f FROM Friend f WHERE (f.requester = :user OR f.receiver = :user) AND f.status = 'ACCEPTED'
    """)
    List<Friend> findFriendsOfUser(@Param("user")  User user);

    Optional<Friend> findByReceiverAndRequester(User receiver, User requester);
    Optional<Friend> findByRequesterUserIdAndReceiverUserId(Long requesterId, Long receiverId);

    @Query("""
        SELECT CASE
            WHEN f.requester.userId = :userId THEN f.receiver.userId
            ELSE f.requester.userId
        END
        FROM Friend f
        WHERE (f.requester.userId = :userId OR f.receiver.userId = :userId)
    """)
    Set<Long> findConnectedUserIds(@Param("userId") Long userId);
}
