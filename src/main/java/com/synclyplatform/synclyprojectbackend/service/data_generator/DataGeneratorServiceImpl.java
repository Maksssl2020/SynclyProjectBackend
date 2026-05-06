package com.synclyplatform.synclyprojectbackend.service.data_generator;

import com.synclyplatform.synclyprojectbackend.dto.media.MediaRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.media.MediaType;
import com.synclyplatform.synclyprojectbackend.dto.post.*;
import com.synclyplatform.synclyprojectbackend.dto.tag.TagDTO;
import com.synclyplatform.synclyprojectbackend.model.post_collection.PostCollection;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user.UserRole;
import com.synclyplatform.synclyprojectbackend.model.user.UserStatus;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.model.user_settings.UserSettings;
import com.synclyplatform.synclyprojectbackend.repository.PostCollectionRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserProfileRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.media.MediaService;
import com.synclyplatform.synclyprojectbackend.service.post.PostService;
import com.synclyplatform.synclyprojectbackend.service.tag.TagService;
import com.synclyplatform.synclyprojectbackend.service.user_settings.UserSettingsService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
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
    private final UserSettingsService userSettingsService;
    private final PostCollectionRepository postCollectionRepository;

    private final Faker faker = new Faker(Locale.ENGLISH);
    private final Random random = new Random();

    private final List<String> techTags = List.of(
            "java", "spring", "backend", "coding", "devlife", "api"
    );

    private final List<String> lifestyleTags = List.of(
            "life", "daily", "thoughts", "mood", "random"
    );

    private final List<String> businessTags = List.of(
            "startup", "business", "productivity", "work", "growth"
    );

    private final List<String> emojis = List.of(
            "🔥", "🚀", "😂", "💡", "✨", "☕", "📌", "😅", "🤔", "🙌"
    );

    @Override
    public void generateUser() throws Exception {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String username = faker.credentials().username();

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
                .accountNonLocked(true)
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

        String avatarUrl = generateAvatarUrl(username);
        byte[] imageBytes;

        try {
            imageBytes = URI.create(avatarUrl).toURL().openStream().readAllBytes();

            MultipartFile avatar = new MockMultipartFile(
                    "avatar",
                    username + "-avatar.png",
                    "image/png",
                    imageBytes
            );

            mediaService.saveUserAvatar(user.getUserId(), avatar);

        } catch (IOException e) {
            throw new RuntimeException("Could not download generated avatar", e);
        }

        userRepository.save(user);

        if (user.getUserSettings() == null) {
            UserSettings userSettings = userSettingsService.createUserSettings(user.getUserId());
            user.setUserSettings(userSettings);
        }

        userRepository.save(user);

        boolean exists = postCollectionRepository.existsByUserUserIdAndTitle(user.getUserId(), "ALL");

        if (!exists) {
            PostCollection postCollection = PostCollection.builder()
                    .title("ALL")
                    .color("#14b8a6")
                    .isDefault(true)
                    .user(user)
                    .build();

            postCollectionRepository.save(postCollection);
        }
    }

    @Override
    public String generateAvatarUrl(String seed) {
        List<String> styles = List.of(
                "adventurer",
                "avataaars",
                "bottts",
                "fun-emoji",
                "lorelei",
                "micah",
                "notionists",
                "personas",
                "pixel-art"
        );

        String style = styles.get(new Random().nextInt(styles.size()));

        return "https://api.dicebear.com/9.x/" + style + "/png?seed=" + seed;
    }

    @Override
    public void generateUserPosts() {
        List<Long> allUserIds = userRepository.findAllUserIds();
        Long randomUserId = allUserIds.get(random.nextInt(allUserIds.size()));
        PostRequestDTO postRequestDTO = getRandomPost();
        postService.save(randomUserId, postRequestDTO);
    }


    @Override
    public PostRequestDTO generatePostToSave() {
        return getRandomPost();
    }

    @NonNull
    private PostRequestDTO getRandomPost() {
        int postTypeIndex = random.nextInt(5);

        return switch (postTypeIndex) {
            case 1 -> generateQuotePost();
            case 2 -> generateLinkPost();
            case 3 -> generatePhotoPost();
            case 4 -> generateVideoPost();
            default -> generateTextPost();
        };
    }

    private VideoPostRequestDTO generateVideoPost() {
        VideoPostRequestDTO videoPostRequestDTO = new VideoPostRequestDTO();
        videoPostRequestDTO.setDescription(faker.lorem().sentence());
        List<String> videoUrls = new ArrayList<>();
        List<String> sampleVideoUrls = List.of(
                "https://download.samplelib.com/mp4/sample-5s.mp4",
                "https://download.samplelib.com/mp4/sample-10s.mp4",
                "https://download.samplelib.com/mp4/sample-15s.mp4",
                "https://download.samplelib.com/mp4/sample-20s.mp4",
                "https://filesamples.com/samples/video/mp4/sample_640x360.mp4",
                "https://filesamples.com/samples/video/mp4/sample_960x400_ocean_with_audio.mp4",
                "https://filesamples.com/samples/video/mp4/sample_1280x720_surfing_with_audio.mp4",
                "https://www.w3schools.com/html/mov_bbb.mp4",
                "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4",
                "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4",
                "https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_1MB.mp4",
                "https://cdn.coverr.co/videos/coverr-working-on-laptop-5175/1080p.mp4",
                "https://cdn.coverr.co/videos/coverr-man-working-on-laptop-1573/1080p.mp4"
        );

        for (int i = 0; i < random.nextInt(3) + 1; i++) {
            int videoUrlIndex = random.nextInt(sampleVideoUrls.size());
            String url = sampleVideoUrls.get(videoUrlIndex);
            videoUrls.add(url);
        }

        List<String> tags = new ArrayList<>(techTags);
        tags.addAll(lifestyleTags);
        tags.addAll(businessTags);

        videoPostRequestDTO.setVideoUrls(videoUrls);
        videoPostRequestDTO.setTags(randomTags(tags));

        return videoPostRequestDTO;
    }

    private PhotoPostRequestDTO generatePhotoPost() {
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

        List<String> tags = new ArrayList<>(techTags);
        tags.addAll(lifestyleTags);
        tags.addAll(businessTags);

        photoPostRequestDTO.setPhotos(photos);
        photoPostRequestDTO.setTags(randomTags(tags));

        return photoPostRequestDTO;
    }

    private LinkPostRequestDTO generateLinkPost() {
        LinkPostRequestDTO linkPostRequestDTO = new LinkPostRequestDTO();
        linkPostRequestDTO.setTitle(faker.book().title());
        linkPostRequestDTO.setDescription(faker.lorem().sentence() + " " + emoji());
        List<String> links = new ArrayList<>();
        Set<String> sampleLinks = Set.of(
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
                "https://shopify.com",
                faker.internet().url(),
                faker.internet().url(),
                faker.internet().url(),
                faker.internet().url(),
                faker.internet().url()
        );

        for (int i = 0; i < random.nextInt(5) + 1; i++) {
            int randomLinkIndex = random.nextInt(sampleLinks.size());
            String link = sampleLinks.stream().toList().get(randomLinkIndex);
            links.add(link);
        }

        linkPostRequestDTO.setLinks(links);
        linkPostRequestDTO.setTags(Set.of("link", randomTag(techTags)));

        return linkPostRequestDTO;
    }

    private QuotePostRequestDTO generateQuotePost() {
        QuotePostRequestDTO quotePostRequestDTO = new QuotePostRequestDTO();

        quotePostRequestDTO.setQuote(faker.famousLastWords().lastWords());
        quotePostRequestDTO.setSource(faker.book().author());
        quotePostRequestDTO.setTags(Set.of("quote", randomTag(lifestyleTags)));

        return quotePostRequestDTO;
    }

    private TextPostRequestDTO generateTextPost() {
        TextPostRequestDTO textPostRequestDTO = new TextPostRequestDTO();
        int style = random.nextInt(3);

        switch (style) {
            case 0 -> {
                textPostRequestDTO.setTitle("Dev thought of the day: " + faker.hacker().noun());
                textPostRequestDTO.setContent(faker.lorem().paragraph(1) + " " + emoji());
                textPostRequestDTO.setTags(randomTags(techTags));
            }
            case 1 -> {
                textPostRequestDTO.setTitle(faker.book().title());
                textPostRequestDTO.setContent(faker.lorem().paragraph(1) + " " + emoji());
                textPostRequestDTO.setTags(randomTags(lifestyleTags));
            }
            case 2 -> {
                textPostRequestDTO.setTitle("Quick update");
                textPostRequestDTO.setContent(
                        "Today I learned that " +
                                faker.company().buzzword() +
                                " can change everything. " +
                                emoji()
                );
                textPostRequestDTO.setTags(randomTags(businessTags));
            }
        }

        return textPostRequestDTO;
    }

    private String emoji() {
        return emojis.get(random.nextInt(emojis.size()));
    }

    private String randomTag(List<String> tags) {
        String chosenTag = tags.get(random.nextInt(tags.size()));

        if (!tagService.tagExists(chosenTag)) {
            tagService.saveCommonTag(chosenTag);
        }

        return chosenTag;
    }

    private Set<String> randomTags(List<String> tags) {
        int additionalTagsFromBackendNumber = random.nextInt(6) + 1;

        List<String> list = tagService.findAllTags().stream()
                .map(TagDTO::getName)
                .toList();

        Set<String> randomTags = new HashSet<>(Set.of(
                randomTag(tags),
                randomTag(tags)
        ));

        int targetSize = randomTags.size() + additionalTagsFromBackendNumber;

        while (randomTags.size() < targetSize) {
            String tagToAdd = list.get(random.nextInt(list.size()));

            if (randomTags.contains(tagToAdd))
                continue;

            randomTags.add(tagToAdd);
        }

        return randomTags;
    }
}
