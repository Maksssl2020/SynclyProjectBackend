package com.synclyplatform.synclyprojectbackend.model.shared_post;

import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User sharedBy;

    @ManyToOne
    private Post originalPost;

    private LocalDateTime sharedAt = LocalDateTime.now();
}
