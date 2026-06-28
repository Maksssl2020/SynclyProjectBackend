package com.synclyplatform.synclyprojectbackend.dto.friend;

import com.synclyplatform.synclyprojectbackend.model.friend.FriendStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestStatusDTO {

    private FriendStatus friendStatus;
}
