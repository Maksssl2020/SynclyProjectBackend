package com.synclyplatform.synclyprojectbackend.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationNotification {

    private String conversationId;
    private String senderUsername;
    private String recipientUsername;
    private Long senderUserId;
    private Long recipientUserId;
    private String messageContent;
}
