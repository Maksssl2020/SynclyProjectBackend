package com.synclyplatform.synclyprojectbackend.service.conversation_message;

import com.synclyplatform.synclyprojectbackend.dto.conversation_message.ConversationMessageRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.conversation_message.ConversationMessage;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.ConversationMessageRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.conversation.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationMessageServiceImpl implements ConversationMessageService {

    private final ConversationMessageRepository conversationMessageRepository;
    private final UserRepository userRepository;
    private final ConversationService conversationService;

    @Override
    public ConversationMessage save(ConversationMessageRequestDTO conversationMessageRequest) throws Exception {
        User foundSender = userRepository.findByUsername(conversationMessageRequest.getSenderUsername())
                .orElseThrow(() -> new Exception("Sender not found."));
        User foundRecipient = userRepository.findByUsername(conversationMessageRequest.getRecipientUsername())
                .orElseThrow(() -> new Exception("Recipient not found."));

        String conversationId = getConversationId(
                conversationMessageRequest.getSenderUsername(),
                conversationMessageRequest.getRecipientUsername(),
                true
        );

        ConversationMessage conversationMessage = ConversationMessage.builder()
                .conversationId(conversationId)
                .senderUsername(conversationMessageRequest.getSenderUsername())
                .recipientUsername(conversationMessageRequest.getRecipientUsername())
                .recipientUserId(foundRecipient.getUserId())
                .senderUserId(foundSender.getUserId())
                .timestamp(LocalDateTime.now())
                .build();

        conversationMessageRepository.save(conversationMessage);

        return conversationMessage;
    }

    @Override
    public List<ConversationMessage> findConversationMessages(String senderUsername, String recipientUsername) throws Exception {
        String conversationId = getConversationId(
                senderUsername,
                recipientUsername,
                false
        );

        return conversationMessageRepository.findAllByConversationId(conversationId);
    }

    private String getConversationId(String senderUsername, String recipientUsername, boolean createNewConversationIfNotExists) throws Exception {
        return conversationService
                .getConversationId(
                        senderUsername,
                        recipientUsername,
                        createNewConversationIfNotExists
                )
                .orElseThrow(() -> new Exception("Cannot find conversation."));
    }
}
