package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.user.AdminUserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserPresenceDTO;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityActionType;
import com.synclyplatform.synclyprojectbackend.model.activity.ActivityTargetType;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import com.synclyplatform.synclyprojectbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<AdminUserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UserRole userRole,
            @RequestParam(required = false) UserStatus userStatus,
            @RequestParam(required = false) String searchQuery
    ) {
        return new ResponseEntity<>(userService.getAllUsers(page, size, userRole, userStatus, searchQuery), HttpStatus.OK);
    }

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
