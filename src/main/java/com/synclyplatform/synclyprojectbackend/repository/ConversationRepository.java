package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.conversation.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    Optional<Conversation> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
    Optional<Conversation> findBySenderUsernameAndRecipientUsername(String senderUsername, String recipientUsername);
    List<Conversation> findAllBySenderId(Long senderId);
}
