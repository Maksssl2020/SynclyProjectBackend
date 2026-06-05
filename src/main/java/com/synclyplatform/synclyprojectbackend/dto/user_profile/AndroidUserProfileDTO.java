package com.synclyplatform.synclyprojectbackend.dto.user_profile;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AndroidUserProfileDTO extends UserProfileDTO {

    private long postsCount;
    private long friendsCount;
}
