package com.synclyplatform.synclyprojectbackend.model.like;

import com.synclyplatform.synclyprojectbackend.model.comment.PostComment;
import com.synclyplatform.synclyprojectbackend.model.post.Post;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "user_post_comment_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_comment_id"})
})
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_comment_id")
    private PostComment postComment;

    private LocalDateTime likedAt;
}
