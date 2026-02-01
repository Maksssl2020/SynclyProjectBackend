package com.synclyplatform.synclyprojectbackend.dto.friend;

import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
import com.synclyplatform.synclyprojectbackend.model.friend.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {

    private Long id;
    private UserDTO requester;
    private UserDTO receiver;
    private FriendStatus status;
    private String createdAt;
}
