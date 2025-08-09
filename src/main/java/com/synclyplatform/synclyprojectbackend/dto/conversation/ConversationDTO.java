package com.synclyplatform.synclyprojectbackend.dto.conversation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ConversationDTO {
    private Long id;
    private String conversationId;
    private String senderUsername;
    private String recipientUsername;
    private String lastMessageContent;
    private String lastMessageTimestamp;
    private Long senderId;
    private Long recipientId;
}
