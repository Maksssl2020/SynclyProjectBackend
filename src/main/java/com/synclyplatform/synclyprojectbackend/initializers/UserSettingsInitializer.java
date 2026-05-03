package com.synclyplatform.synclyprojectbackend.initializers;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.model.user_settings.UserSettings;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.user_settings.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@RequiredArgsConstructor
//public class UserSettingsInitializer implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final UserSettingsService userSettingsService;
//
//
//    @Override
//    public void run(String... args) throws Exception {
//        List<User> users = userRepository.findAll();
//
//        users.forEach(user -> {
//            try {
//                if (user.getUserSettings() == null) {
//                    UserSettings userSettings = userSettingsService.createUserSettings(user.getUserId());
//                    user.setUserSettings(userSettings);
//                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        userRepository.saveAll(users);
//    }
//}
