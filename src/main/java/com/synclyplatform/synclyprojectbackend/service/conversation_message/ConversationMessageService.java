package com.synclyplatform.synclyprojectbackend.service.conversation_message;

import com.synclyplatform.synclyprojectbackend.dto.conversation_message.ConversationMessageRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.conversation_message.ConversationMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConversationMessageService {

    ConversationMessage save(ConversationMessageRequestDTO conversationMessageRequest) throws Exception;
    List<ConversationMessage> findConversationMessages(String senderUsername, String recipientUsername) throws Exception;
}
