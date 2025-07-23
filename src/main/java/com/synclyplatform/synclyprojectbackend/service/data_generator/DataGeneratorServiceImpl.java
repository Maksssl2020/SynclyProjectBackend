package com.synclyplatform.synclyprojectbackend.service.data_generator;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.PostCollectionRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.media.MediaService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class DataGeneratorServiceImpl implements DataGeneratorService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PostCollectionRepository postCollectionRepository;
    private final MediaService mediaService;
    private final PasswordEncoder passwordEncoder;

    private final Faker faker = new Faker(Locale.ENGLISH);

    @Override
    public User generateUser() throws MalformedURLException {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String username = faker.internet().username();

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .username(username)
                .password(passwordEncoder.encode("PA$$W0RD123"))
                .createdAt(LocalDateTime.now())
                .lastActive(LocalDateTime.now())
                .role(UserRole.REGISTERED)
                .status(UserStatus.OFFLINE)
                .build();

        userRepository.save(user);


        UserProfile userProfile = UserProfile.builder()
                .displayName(firstName + " " + lastName)
                .bio(faker.lorem().sentence())
                .location(faker.address().fullAddress())
                .birthday(faker.timeAndDate().birthday())
                .website(faker.internet().url())
                .user(user)
                .build();

        user.setUserProfile(userProfile);
        userProfileRepository.save(userProfile);

        String base64Image = faker.avatar().image();
        byte[] imageBytes;

        try {
            imageBytes = new URL(base64Image).openStream().readAllBytes();
            MultipartFile avatar = new MockMultipartFile("avatar", "avatar.png", "image/png", imageBytes);
            mediaService.saveUserAvatar(user.getUserId(), avatar);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userRepository.save(user);
    }
}
