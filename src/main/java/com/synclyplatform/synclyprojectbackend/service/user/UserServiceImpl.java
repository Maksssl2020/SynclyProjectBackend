package com.synclyplatform.synclyprojectbackend.service.user;

import com.synclyplatform.synclyprojectbackend.dto.user.AdminUserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.dto.user.UserPresenceDTO;
import com.synclyplatform.synclyprojectbackend.model.image.Image;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.model.utils.TimestampSortOption;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.mapper.UserMapper;
import com.synclyplatform.synclyprojectbackend.service.user_profile.UserProfileService;
import com.synclyplatform.synclyprojectbackend.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserProfileService userProfileService;

    private final Set<String> allowedUserSortFields = Set.of(
            "role",
            "status",
            "postCont",
            "followers",
            "createdAt",
            "lastActive"
    );

    @Override
    public UserDTO findUserById(Long id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return userMapper.toDTO(foundUser);
    }

    @Override
    public User disconnect(long userId) {
        User foundUser = userRepository.findById(userId)
                .orElse(null);

        if (foundUser != null) {
            foundUser.setStatus(UserStatus.OFFLINE);
            userRepository.save(foundUser);
            return foundUser;
        }

        return null;
    }

    @Override
    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(UserStatus.ONLINE);
    }

    @Override
    public List<UserDTO> searchUsers(String query) {
        return userRepository.searchUserByQuery(query.trim()).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserPresenceDTO getUserPresenceStatus(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    UserStatus userStatus = user.getStatus();
                    LocalDateTime lastSeen = user.getLastActive();
                    return UserPresenceDTO.builder()
                            .userId(userId)
                            .online(userStatus == UserStatus.ONLINE)
                            .lastSeen(lastSeen.toString())
                            .build();
                })
                .orElse(null);
    }

    @Override
    public Page<AdminUserDTO> getAllUsers(int page, int size, UserRole userRole, UserStatus userStatus, String searchQuery, String sortBy, String sortDirection) {
        String normalizedSearchQuery = searchQuery == null ? "" : searchQuery.trim();
        boolean searchEnabled = !normalizedSearchQuery.isBlank();
        Pageable pageable = PageRequest.of(page, size);

        log.info("sortBy: {}, direction: {}", sortBy, sortDirection);

        if (!allowedUserSortFields.contains(sortBy)) {
            sortBy = "createdAt";
        }

        if (sortBy.equals("followersCount")) {
            Page<User> users = sortDirection.equals("asc")
                    ? userRepository.findAllFilteredForAdminAndSortedByFollowersCountAsc(userRole, userStatus, normalizedSearchQuery, searchEnabled, pageable)
                    : userRepository.findAllFilteredForAdminAndSortedByFollowersCountDesc(userRole, userStatus, normalizedSearchQuery, searchEnabled, pageable);

            return users.map(userMapper::toAdminUserDTO);
        }

        if (sortBy.equals("postCount")) {
            Page<User> users = sortDirection.equals("asc")
                    ? userRepository.findAllFilteredForAdminAndSortedByPostCountAsc(userRole, userStatus, normalizedSearchQuery, searchEnabled, pageable)
                    : userRepository.findAllFilteredForAdminAndSortedByPostCountDesc(userRole, userStatus, normalizedSearchQuery, searchEnabled, pageable);

            return users.map(userMapper::toAdminUserDTO);
        }

        Sort sortOption = Sort.by(sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);

        pageable = PageRequest.of(page, size, sortOption);
        Page<User> users = userRepository.findAllFilteredForAdmin(userRole, userStatus, normalizedSearchQuery, searchEnabled, pageable);

        return users.map(userMapper::toAdminUserDTO);
    }
}
