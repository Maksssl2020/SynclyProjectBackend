package com.synclyplatform.synclyprojectbackend.service.data_generator;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
public interface DataGeneratorService {
    void generateUser() ;
    void generateUserPosts() ;
}
