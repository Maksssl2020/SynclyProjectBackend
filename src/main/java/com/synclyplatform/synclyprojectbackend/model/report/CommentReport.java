package com.synclyplatform.synclyprojectbackend.model.report;

import com.synclyplatform.synclyprojectbackend.model.comment.PostComment;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentReport extends Report {

    @ManyToOne
    private PostComment comment;
}
