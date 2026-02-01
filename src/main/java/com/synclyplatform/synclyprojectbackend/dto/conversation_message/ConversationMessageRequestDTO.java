package com.synclyplatform.synclyprojectbackend.dto.conversation_message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationMessageRequestDTO {

    private String senderUsername;
    private String recipientUsername;
    private String message;
}
