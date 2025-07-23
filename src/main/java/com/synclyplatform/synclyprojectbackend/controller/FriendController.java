package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.friend.FriendDTO;
import com.synclyplatform.synclyprojectbackend.dto.friend.FriendUserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.friend.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping()
    public ResponseEntity<List<FriendUserDTO>> getFriendList(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(friendService.getFriendList(user.getUserId()), HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendDTO>> getPendingRequests(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(friendService.getPendingRequests(user.getUserId()), HttpStatus.OK);
    }

    @GetMapping("/suggested")
    public ResponseEntity<List<UserDTO>> getSuggestedRequests(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(friendService.getSuggestedFriends(user.getUserId()), HttpStatus.OK);
    }

    @PostMapping("/send/request")
    public ResponseEntity<HttpStatus> sendFriendRequest(
            @RequestParam("requesterId") Long requesterId,
            @RequestParam("receiverId") Long receiverId
    ) {
        friendService.sendFriendRequest(requesterId, receiverId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/accept/request")
    public ResponseEntity<HttpStatus> acceptFriendRequest(
            @RequestParam("requestId") Long requestId
    ) {
        friendService.acceptFriendRequest(requestId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/block/user")
    public ResponseEntity<HttpStatus> blockUser(
            @RequestParam("userId") Long userId,
            @RequestParam("blockedUserId") Long blockedUserId
    ) {
        friendService.blockUser(userId, blockedUserId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/decline/request")
    public ResponseEntity<HttpStatus> declineFriendRequest(
            @RequestParam("requestId") Long requestId
    ) {
        friendService.declineFriendRequest(requestId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/remove/friend")
    public ResponseEntity<HttpStatus> removeFriend(
            @RequestParam("userId") Long userId,
            @RequestParam("friendId") Long friendId
    ) {
        friendService.removeFriend(userId, friendId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
