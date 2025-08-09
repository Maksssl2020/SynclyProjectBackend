package com.synclyplatform.synclyprojectbackend.service.conversation;

import com.synclyplatform.synclyprojectbackend.dto.conversation.ConversationDTO;
import com.synclyplatform.synclyprojectbackend.model.conversation.Conversation;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ConversationService {

    String save(String senderUsername, String recipientUsername);
    String save(Long senderId, Long recipientId);
    Optional<String> getConversationId(String senderUsername, String recipientUsername, boolean createNewConversationIfNotExists);
    Optional<String> getConversationId(Long senderId, Long recipientId, boolean createNewConversationIfNotExists);
    List<ConversationDTO> findAllByUser(User user);
}
