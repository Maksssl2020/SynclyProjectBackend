package com.synclyplatform.synclyprojectbackend.repository;

import com.synclyplatform.synclyprojectbackend.model.conversation_message.ConversationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessage,Long> {

    List<ConversationMessage> findAllByConversationId(String conversationId);

    @Query("""
        SELECT cm FROM ConversationMessage cm
        WHERE cm.conversationId = :conversationId
          AND cm.recipientUsername = :username
          AND cm.read = false
    """)
    List<ConversationMessage> findUnreadMessages(
            @Param("conversationId") String conversationId,
            @Param("username") String username
    );

    @Query("""
        SELECT cm FROM ConversationMessage cm
        WHERE cm.conversationId = :conversationId
        AND cm.timestamp = (
            SELECT MAX(m.timestamp) FROM  ConversationMessage m WHERE m.conversationId = cm.conversationId
        )
        ORDER BY cm.timestamp DESC
    """)
    Optional<ConversationMessage> findLastMessageByConversation(@Param("conversationId") String conversationId);
}
