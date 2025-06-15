package com.synclyplatform.synclyprojectbackend.service.conversation;

import com.synclyplatform.synclyprojectbackend.model.conversation.Conversation;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.ConversationRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    @Override
    public Optional<String> getConversationId(
            String senderUsername,
            String recipientUsername,
            boolean createNewConversationIfNotExists
    ) {
        return conversationRepository.findBySenderUsernameAndRecipientUsername(senderUsername, recipientUsername)
                .map(Conversation::getConversationId)
                .or(() -> {
                    if (createNewConversationIfNotExists) {
                        String conversationId = save(senderUsername, recipientUsername);
                        return Optional.of(conversationId);
                    }

                    return  Optional.empty();
                });
    }

    @Override
    public String save(String senderUsername, String recipientUsername) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findByUsername(recipientUsername)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        String conversationId = String.format("%s_%s", senderUsername, recipientUsername);

        Conversation senderRecipient = Conversation.builder()
                .conversationId(conversationId)
                .senderId(sender.getUserId())
                .recipientId(recipient.getUserId())
                .senderUsername(senderUsername)
                .recipientUsername(recipientUsername)
                .build();

        Conversation recipientSender = Conversation.builder()
                .conversationId(conversationId)
                .senderId(recipient.getUserId())
                .recipientId(sender.getUserId())
                .senderUsername(recipientUsername)
                .recipientUsername(senderUsername)
                .build();

        conversationRepository.save(senderRecipient);
        conversationRepository.save(recipientSender);

        return conversationId;
    }
}
