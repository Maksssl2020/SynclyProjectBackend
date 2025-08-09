package com.synclyplatform.synclyprojectbackend.service.conversation_message;

import com.synclyplatform.synclyprojectbackend.dto.conversation_message.ConversationMessageRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.conversation_message.ConversationMessage;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConversationMessageService {

    void markAsRead(String conversationId, String username);
    ConversationMessage save(ConversationMessageRequestDTO conversationMessageRequest) throws Exception;
    List<ConversationMessage> findConversationMessages(String senderUsername, String recipientUsername) throws Exception;
    List<ConversationMessage> findConversationMessages(User user, Long recipientId) throws Exception;
}
