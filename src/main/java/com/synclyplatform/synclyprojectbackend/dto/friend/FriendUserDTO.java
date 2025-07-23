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
public class FriendUserDTO {
    private UserDTO user;
    private Long mutualFriendsCount;
}
