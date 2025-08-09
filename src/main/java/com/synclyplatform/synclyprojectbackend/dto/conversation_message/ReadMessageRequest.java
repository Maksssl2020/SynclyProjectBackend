package com.synclyplatform.synclyprojectbackend.dto.conversation_message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReadMessageRequest {

    private String conversationId;
}
