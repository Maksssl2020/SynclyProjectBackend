package com.synclyplatform.synclyprojectbackend.model.user;

import com.synclyplatform.synclyprojectbackend.model.friend.Friend;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.post_collection.PostCollection;
import com.synclyplatform.synclyprojectbackend.model.tag.Tag;
import com.synclyplatform.synclyprojectbackend.model.user_profile.UserProfile;
import com.synclyplatform.synclyprojectbackend.model.user_settings.UserSettings;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
@EqualsAndHashCode(exclude = {"userProfile", "userSettings"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required.")
    private String username;

    @Column(unique = true, nullable = false)
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime lastActive;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserProfile userProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserSettings userSettings;

    @ManyToMany
    @JoinTable(name = "user_followed_tags",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> followedTags = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_follows_profile",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private List<UserProfile> followedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCollection> postCollections = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_saved_posts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> savedPosts = new HashSet<>();

    @OneToMany(mappedBy = "requester")
    private List<Friend> sentFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Friend> receivedFriendRequests = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
