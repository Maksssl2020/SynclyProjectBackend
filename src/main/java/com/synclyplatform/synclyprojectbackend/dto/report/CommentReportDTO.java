package com.synclyplatform.synclyprojectbackend.dto.report;

import com.synclyplatform.synclyprojectbackend.dto.comment.PostCommentDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentReportDTO extends ReportDTO {

    private PostCommentDTO comment;
}
