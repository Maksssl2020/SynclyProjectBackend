package com.synclyplatform.synclyprojectbackend.service.data_generator;

import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.media.MediaType;
import com.synclyplatform.synclyprojectbackend.dto.post.*;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.repository.PostCollectionRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.media.MediaService;
import com.synclyplatform.synclyprojectbackend.service.post.PostService;
import com.synclyplatform.synclyprojectbackend.service.tag.TagService;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class DataGeneratorServiceImpl implements DataGeneratorService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final MediaService mediaService;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;
    private final TagService tagService;

    private final Faker faker = new Faker(Locale.ENGLISH);

    @Override
    public void generateUser() {
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

        userRepository.save(user);
    }

    @Override
    public void generateUserPosts() {
        List<Long> allUserIds = userRepository.findAllUserIds();
        Random random = new Random();
        Long randomUserId = allUserIds.get(random.nextInt(allUserIds.size()));
        int postTypeIndex = random.nextInt(5);
        Set<String> tags = new HashSet<>();

        List<String> allTags = tagService.findAllTags().stream().map(TagDTO::getName).toList();

        for (int i = 0; i < random.nextInt(12) + 1; i++) {
            String tag;

            do {
                tag = allTags.get(random.nextInt(allTags.size()));
            } while (tags.contains(tag));

            tags.add(tag);
        }

        switch (postTypeIndex) {
            case 0 -> {
                TextPostRequestDTO textPostRequestDTO = new TextPostRequestDTO();
                textPostRequestDTO.setTitle(faker.book().title());
                textPostRequestDTO.setContent(faker.lorem().paragraph(3));
                textPostRequestDTO.setTags(tags.stream().toList());

                postService.save(randomUserId, textPostRequestDTO);
            }
            case 1 -> {
                QuotePostRequestDTO quotePostRequestDTO = new QuotePostRequestDTO();
                quotePostRequestDTO.setQuote(faker.hitchhikersGuideToTheGalaxy().quote());
                quotePostRequestDTO.setSource(faker.book().title());
                quotePostRequestDTO.setTags(tags.stream().toList());

                postService.save(randomUserId, quotePostRequestDTO);
            }
            case 2 -> {
                LinkPostRequestDTO linkPostRequestDTO = new LinkPostRequestDTO();
                linkPostRequestDTO.setTitle(faker.book().title());
                linkPostRequestDTO.setDescription(faker.lorem().sentence());
                List<String> links = new ArrayList<>();
                List<String> sampleLinks = List.of(
                        "https://github.com",
                        "https://stackoverflow.com",
                        "https://spring.io",
                        "https://reactjs.org",
                        "https://openai.com",
                        "https://medium.com",
                        "https://netflix.com",
                        "https://youtube.com",
                        "https://twitter.com",
                        "https://reddit.com",
                        "https://linkedin.com",
                        "https://dev.to",
                        "https://producthunt.com",
                        "https://nytimes.com",
                        "https://bbc.com",
                        "https://cnn.com",
                        "https://wikipedia.org",
                        "https://amazon.com",
                        "https://ebay.com",
                        "https://shopify.com"
                );

                for (int i = 0; i < random.nextInt(5) + 1; i++) {
                    int randomLinkIndex = random.nextInt(sampleLinks.size());
                    links.add(sampleLinks.get(randomLinkIndex));
                }

                linkPostRequestDTO.setLinks(links);
                linkPostRequestDTO.setTags(tags.stream().toList());

                postService.save(randomUserId, linkPostRequestDTO);
            }
            case 3 -> {
                PhotoPostRequestDTO photoPostRequestDTO = new PhotoPostRequestDTO();
                photoPostRequestDTO.setCaption(faker.lorem().sentence());
                List<MediaRequestDTO> photos = new ArrayList<>();

                for (int i = 0; i < random.nextInt(5) + 1; i++) {
                    String photoUrl = "https://picsum.photos/seed/" + UUID.randomUUID() + "/600/400";
                    MediaRequestDTO mediaRequestDTO = new MediaRequestDTO();
                    mediaRequestDTO.setUrl(photoUrl);
                    mediaRequestDTO.setMediaType(MediaType.IMAGE);
                    photos.add(mediaRequestDTO);
                }

                photoPostRequestDTO.setPhotos(photos);
                photoPostRequestDTO.setTags(tags.stream().toList());

                postService.save(randomUserId, photoPostRequestDTO);
            }
            case 4 -> {
                VideoPostRequestDTO videoPostRequestDTO = new VideoPostRequestDTO();
                videoPostRequestDTO.setDescription(faker.lorem().sentence());
                List<MediaRequestDTO> videos = new ArrayList<>();
                List<String> sampleVideoUrls = List.of(
                        "https://www.w3schools.com/html/mov_bbb.mp4",
                        "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                        "https://file-examples.com/storage/fe3cb77895a1c0a25c78f13/2017/04/file_example_MP4_480_1_5MG.mp4",
                        "https://filesamples.com/samples/video/mp4/sample_640x360.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4"

                );


                for (int i = 0; i < random.nextInt(3) + 1; i++) {
                    MediaRequestDTO mediaRequestDTO = new MediaRequestDTO();
                    int videoUrlIndex = random.nextInt(sampleVideoUrls.size());

                    mediaRequestDTO.setUrl(sampleVideoUrls.get(videoUrlIndex));
                    mediaRequestDTO.setMediaType(MediaType.VIDEO);
                    videos.add(mediaRequestDTO);
                }

                videoPostRequestDTO.setVideos(videos);
                videoPostRequestDTO.setTags(tags.stream().toList());

                postService.save(randomUserId, videoPostRequestDTO);
            }
        }

    }
}
