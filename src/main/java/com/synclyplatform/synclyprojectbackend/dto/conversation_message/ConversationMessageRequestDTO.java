package com.synclyplatform.synclyprojectbackend.dto.conversation_message;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMessageRequestDTO {

    private String senderUsername;
    private String recipientUsername;
    private String message;
}
