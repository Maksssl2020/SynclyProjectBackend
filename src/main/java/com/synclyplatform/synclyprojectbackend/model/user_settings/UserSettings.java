package com.synclyplatform.synclyprojectbackend.model.user_settings;

import com.synclyplatform.synclyprojectbackend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "user")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSettingsId;

    private boolean publicProfile = true;
    private boolean showEmail = false;
    private boolean showLocation = true;
    private boolean showOnlineStatus = true;
    private boolean twoFactorAuthentication = false;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
