package com.synclyplatform.synclyprojectbackend.model.user_profile;

import com.synclyplatform.synclyprojectbackend.model.image.Image;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class UserProfile {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long userProfileId;

    private String displayName;
    private String bio;
    private String location;
    private LocalDate birthday;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image profileImage;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
