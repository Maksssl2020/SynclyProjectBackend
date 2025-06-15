package com.synclyplatform.synclyprojectbackend.service.conversation;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ConversationService {

    String save(String senderUsername, String recipientUsername);
    Optional<String> getConversationId(String senderUsername, String recipientUsername, boolean createNewConversationIfNotExists);
}
