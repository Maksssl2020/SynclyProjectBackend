package com.synclyplatform.synclyprojectbackend.model.report;

import com.synclyplatform.synclyprojectbackend.model.post.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class PostReport extends Report {

    @ManyToOne
    private Post post;
}
