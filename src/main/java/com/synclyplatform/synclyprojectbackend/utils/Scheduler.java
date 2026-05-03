package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.dto.post.PostRequestDTO;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.data_generator.DataGeneratorService;
import com.synclyplatform.synclyprojectbackend.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final PostService postService;
    private final UserRepository userRepository;
    private final DataGeneratorService dataGeneratorService;

    @Scheduled(cron = "0 */10 * * * *")
    public void GeneratePost() {
        List<Long> allUserIds = userRepository.findAllUserIds();

        if (allUserIds.isEmpty()) {
            return;
        }

        int randomIndex = new Random().nextInt(allUserIds.size());
        Long randomUserId = allUserIds.get(randomIndex);

        if (!userRepository.existsById(randomUserId) || userRepository.isAdminByUserId(randomUserId)) {
            return;
        }

        PostRequestDTO generated = dataGeneratorService.generatePostToSave();
        postService.save(randomUserId, generated);
    }
}
