//package com.synclyplatform.synclyprojectbackend.initializers;
//
//import com.synclyplatform.synclyprojectbackend.model.post_collection.PostCollection;
//import com.synclyplatform.synclyprojectbackend.model.user.User;
//import com.synclyplatform.synclyprojectbackend.repository.PostCollectionRepository;
//import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class PostCollectionInitializer implements ApplicationRunner {
//
//
//    private final UserRepository userRepository;
//    private final PostCollectionRepository postCollectionRepository;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        List<User> users = userRepository.findAll();
//
//        for (User user : users) {
//            boolean exists = postCollectionRepository.existsByUserUserIdAndTitle(user.getUserId(), "ALL");
//
//            if (!exists) {
//                PostCollection postCollection = PostCollection.builder()
//                        .title("ALL")
//                        .color("#14b8a6")
//                        .isDefault(true)
//                        .user(user)
//                        .build();
//
//                postCollectionRepository.save(postCollection);
//            }
//        }
//    }
//}
