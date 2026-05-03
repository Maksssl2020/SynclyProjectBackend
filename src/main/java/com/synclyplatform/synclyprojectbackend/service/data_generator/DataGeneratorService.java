package com.synclyplatform.synclyprojectbackend.service.data_generator;

import com.synclyplatform.synclyprojectbackend.dto.post.PostRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
public interface DataGeneratorService {

    void generateUser() throws Exception;
    void generateUserPosts();
    String generateAvatarUrl(String seed);
    PostRequestDTO generatePostToSave();
}
