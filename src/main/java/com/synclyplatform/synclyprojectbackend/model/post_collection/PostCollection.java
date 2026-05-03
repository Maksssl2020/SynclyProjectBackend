package com.synclyplatform.synclyprojectbackend.model.post_collection;

import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_title",
                columnNames = {"user_id", "title"}
        )
)
public class PostCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String color;

    private boolean isDefault = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "post_collection_posts",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> posts = new HashSet<>();
}
