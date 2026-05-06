package com.synclyplatform.synclyprojectbackend.mapper;

import com.synclyplatform.synclyprojectbackend.dto.user.AdminUserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.repository.PostRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PostRepository postRepository;
    private final UserProfileMapper userProfileMapper;

    public UserDTO toDTO(User user) {
        Long postCount = postRepository.countByAuthorUserId(user.getUserId());

        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt().toString())
                .lastActive(user.getLastActive() == null ? "" :  user.getLastActive().toString())
                .status(!user.isAccountNonLocked() ? UserStatus.BLOCKED : user.getStatus())
                .userProfile(userProfileMapper.toDTO(user.getUserProfile()))
                .postCount(postCount)
                .build();
    }

    public AdminUserDTO toAdminUserDTO(User user) {
        Long postCount = postRepository.countByAuthorUserId(user.getUserId());

        return AdminUserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt().toString())
                .lastActive(user.getLastActive() == null ? "" :  user.getLastActive().toString())
                .status(!user.isAccountNonLocked() ? UserStatus.BLOCKED : user.getStatus())
                .postCount(postCount)
                .avatar(user.getUserProfile().getProfileImage())
                .followersCount(user.getUserProfile().getFollowers().size())
                .build();
    }
}
