package com.synclyplatform.synclyprojectbackend.dto.friend;

import com.synclyplatform.synclyprojectbackend.dto.user.UserDTO;
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
    private String status;
    private String createdAt;
}
