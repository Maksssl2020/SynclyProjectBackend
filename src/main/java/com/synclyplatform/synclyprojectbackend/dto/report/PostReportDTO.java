package com.synclyplatform.synclyprojectbackend.dto.report;

import com.synclyplatform.synclyprojectbackend.dto.post.PostDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostReportDTO extends ReportDTO {

    private PostDTO post;
}
