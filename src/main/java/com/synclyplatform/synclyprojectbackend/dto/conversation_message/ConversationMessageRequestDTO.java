package com.synclyplatform.synclyprojectbackend.dto.conversation_message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMessageRequestDTO {

    private String senderUsername;
    private String recipientUsername;
    private String message;
}
