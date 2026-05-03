package com.synclyplatform.synclyprojectbackend.service.authentication;

import com.synclyplatform.synclyprojectbackend.dto.authentication.*;
import com.synclyplatform.synclyprojectbackend.dto.two_factor_code.TwoFactorVerificationRequestDTO;
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
import com.synclyplatform.synclyprojectbackend.service.login_history.LoginHistoryService;
import com.synclyplatform.synclyprojectbackend.service.two_factor_code.TwoFactorCodeService;
import com.synclyplatform.synclyprojectbackend.service.user_profile.UserProfileService;
import com.synclyplatform.synclyprojectbackend.service.user_settings.UserSettingsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final LoginHistoryService loginHistoryService;
    private final TwoFactorCodeService twoFactorCodeService;

    @Value("${spring.security.cloudflare.turnstile.secret-key}")
    private String TURNSTILE_SECRET_KEY;
    private static final String VERIFY_TURNSTILE_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

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
                .accountNonLocked(true)
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


        if (user.getPostCollections() == null) {
            user.setPostCollections(new ArrayList<>());
            user.getPostCollections().add(postCollection);
        } else {
            user.getPostCollections().add(postCollection);
        }

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
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        user.setStatus(UserStatus.ONLINE);

        boolean isTwoFactorAuthentication = user.getUserSettings().isTwoFactorAuthentication();

        if (isTwoFactorAuthentication) {
            twoFactorCodeService.generateTwoFactorCode(user.getUserId(), user.getEmail());
        }

        return createAuthenticationResponse(user, isTwoFactorAuthentication);
    }

    @Override
    public AndroidAppAuthenticationResponseDTO authenticateAndroidUser(AuthenticationRequestDTO authenticationRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        user.setStatus(UserStatus.ONLINE);
        return createAuthenticationResponseAndroid(user);
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

    @Override
    public TurnstileResponseDTO verifyTurnstile(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = String.format("secret=%s&response=%s", TURNSTILE_SECRET_KEY, token);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<TurnstileResponseDTO> response = restTemplate.exchange(
                VERIFY_TURNSTILE_URL,
                HttpMethod.POST,
                request,
                TurnstileResponseDTO.class
        );

        return response.getBody();
    }

    @Override
    public AuthenticationResponseDTO validate2FARequest(TwoFactorVerificationRequestDTO twoFactorVerificationRequest, HttpServletRequest httpServletRequest) throws Exception {
        User foundUser = userRepository.findById(twoFactorVerificationRequest.getUserId())
                .orElseThrow(() -> new Exception("User not found, id: " + twoFactorVerificationRequest.getUserId()));

        boolean isValid = twoFactorCodeService.verifyTwoFactorCode(foundUser.getUserId(), twoFactorVerificationRequest.getCode());

        if (!isValid) {
            throw new Exception("Nieprawidłowe dane autoryzacji.");
        }

        loginHistoryService.saveLoginHistory(foundUser.getUserId(), httpServletRequest);
        return createAuthenticationResponse(foundUser, false);
    }

    private AuthenticationResponseDTO createAuthenticationResponse(User user, boolean requiresTwoFactorAuthentication) {
        String accessToken = null;

        if (!requiresTwoFactorAuthentication) {
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("username", user.getUsername());

            accessToken = jwtService.generateJwtToken(claims, user);
        }

        return AuthenticationResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileImage(user.getUserProfile().getProfileImage())
                .accessToken(accessToken)
                .requiresTwoFactorAuthentication(requiresTwoFactorAuthentication)
                .role(user.getRole().toString())
                .build();
    }

    private AndroidAppAuthenticationResponseDTO createAuthenticationResponseAndroid(User user) {
        return AndroidAppAuthenticationResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .role(user.getRole().toString())
                .build();
    }
}
