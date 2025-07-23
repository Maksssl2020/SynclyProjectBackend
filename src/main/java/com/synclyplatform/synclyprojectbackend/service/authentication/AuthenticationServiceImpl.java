package com.synclyplatform.synclyprojectbackend.service.authentication;

import com.synclyplatform.synclyprojectbackend.dto.authentication.AuthenticationRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.AuthenticationResponseDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.ChangePasswordRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.authentication.RegisterRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.user_profile.UserProfileRequestDTO;
import com.synclyplatform.synclyprojectbackend.exception.UserAlreadyExistsException;
import com.synclyplatform.synclyprojectbackend.model.post_collection.PostCollection;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.model.user_settings.UserSettings;
import com.synclyplatform.synclyprojectbackend.repository.PostCollectionRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.security.JwtService;
import com.synclyplatform.synclyprojectbackend.service.user_profile.UserProfileService;
import com.synclyplatform.synclyprojectbackend.service.user_settings.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserProfileService userProfileService;
    private final UserSettingsService userSettingsService;
    private final PostCollectionRepository postCollectionRepository;

    @Override
    public void register(RegisterRequestDTO registerRequest) throws Exception {
        validateRegisterRequest(registerRequest);

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(UserRole.REGISTERED)
                .createdAt(LocalDateTime.now())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .build();


        userRepository.save(user);

        UserProfile userProfile = userProfileService.createUserProfile(
                UserProfileRequestDTO.builder()
                        .displayName(String.format("%s %s", user.getFirstName(), user.getLastName()))
                        .userId(user.getUserId())
                        .build()
        );

        UserSettings userSettings = userSettingsService.createUserSettings(user.getUserId());

        PostCollection postCollection = PostCollection.builder()
                .title("ALL")
                .color("#14b8a6")
                .isDefault(true)
                .user(user)
                .build();

        user.getPostCollections().add(postCollection);

        user.setUserProfile(userProfile);
        user.setUserSettings(userSettings);

        userRepository.save(user);
        postCollectionRepository.save(postCollection);
    }

    private void validateRegisterRequest(RegisterRequestDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException(String.format("Username %s is already taken.", registerRequest.getUsername()));
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException(String.format("E-mail address %s is already taken.", registerRequest.getEmail()));
        }
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        user.setStatus(UserStatus.ONLINE);
        return createAuthenticationResponse(user);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequestDTO changePasswordRequest) throws Exception {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), foundUser.getPassword())) {
            throw new Exception("Incorrect old password!");
        }

        foundUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(foundUser);
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private AuthenticationResponseDTO createAuthenticationResponse(User user) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        String accessToken = jwtService.generateJwtToken(claims, user);

        return AuthenticationResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .accessToken(accessToken)
                .role(user.getRole().toString())
                .build();
    }
}
