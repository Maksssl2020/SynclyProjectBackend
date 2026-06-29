package com.synclyplatform.synclyprojectbackend.dto.conversation;

import com.synclyplatform.synclyprojectbackend.model.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ConversationDTO {
    private Long id;
    private Long senderId;
    private Long recipientId;
    private Long lastMessageSenderId;
    private String conversationId;
    private String senderUsername;
    private String recipientUsername;
    private String lastMessageContent;
    private String lastMessageTimestamp;
    private Image recipientAvatar;
}
