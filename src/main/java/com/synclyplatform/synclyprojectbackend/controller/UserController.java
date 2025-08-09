package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserPresenceDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public User disconnect(
            @Payload Long userId
    ) {
        return userService.disconnect(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> search(@RequestParam String query) {
        return new ResponseEntity<>(userService.searchUsers(query), HttpStatus.OK);
    }

    @GetMapping("/online")
    public ResponseEntity<List<User>> findOnlineUsers() {
        return new ResponseEntity<>(userService.findConnectedUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.findUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/presence/{userId}/status")
    public ResponseEntity<UserPresenceDTO> findUserStatusById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserPresenceStatus(userId), HttpStatus.OK);
    }
}
