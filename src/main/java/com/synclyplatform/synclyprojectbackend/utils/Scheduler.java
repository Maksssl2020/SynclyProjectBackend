package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.dto.post.PostRequestDTO;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.data_generator.DataGeneratorService;
import com.synclyplatform.synclyprojectbackend.service.post.PostService;
import com.synclyplatform.synclyprojectbackend.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final TagService tagService;
    private final PostService postService;
    private final UserRepository userRepository;
    private final DataGeneratorService dataGeneratorService;
    private final Random random = new Random();

    @Scheduled(cron = "0 */30 * * * *")
    public void generatePost() {
        List<Long> allUserIds = userRepository.findAllUserIds();

        if (allUserIds.isEmpty()) {
            return;
        }

        int randomNumber = random.nextInt(10);
        int amountOfRandomPosts = 0;

        if (randomNumber <= 3) {
            amountOfRandomPosts = 1;
        } else if (randomNumber <= 5) {
            amountOfRandomPosts = 2;
        } else if (randomNumber <= 7){
            amountOfRandomPosts = 3;
        } else {
            amountOfRandomPosts = 4;
        }

        for (int i = 0; i < amountOfRandomPosts; i++) {
            int randomIndex = random.nextInt(allUserIds.size());
            Long randomUserId = allUserIds.get(randomIndex);

            if (!userRepository.existsById(randomUserId) || userRepository.isAdminByUserId(randomUserId)) {
                continue;
            }

            PostRequestDTO generated = dataGeneratorService.generatePostToSave();
            postService.save(randomUserId, generated);
        }
    }

    @Scheduled(cron = "0 */45 * * * *")
    public void generateUser() throws Exception {
        int randomNumber = random.nextInt(10);
        int amountOfRandomUsers = 0;

        if (randomNumber <= 5) {
            amountOfRandomUsers =1;
        } else if (randomNumber <= 8) {
            amountOfRandomUsers = 2;
        } else {
            amountOfRandomUsers = 3;
        }

        for (int i = 0; i < amountOfRandomUsers; i++) {
            dataGeneratorService.generateUser();
        }
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void updateTrendingTags() {
        tagService.updateTrendingTags();
    }
}
