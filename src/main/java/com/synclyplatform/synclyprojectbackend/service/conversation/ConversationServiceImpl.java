package com.synclyplatform.synclyprojectbackend.service.conversation;

import com.synclyplatform.synclyprojectbackend.dto.conversation.ConversationDTO;
import com.synclyplatform.synclyprojectbackend.model.conversation.Conversation;
import com.synclyplatform.synclyprojectbackend.model.conversation_message.ConversationMessage;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.ConversationMessageRepository;
import com.synclyplatform.synclyprojectbackend.repository.ConversationRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationMessageRepository conversationMessageRepository;

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
    public Optional<String> getConversationId(Long senderId, Long recipientId, boolean createNewConversationIfNotExists) {
        return conversationRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(Conversation::getConversationId)
                .or(() -> {
                    if(createNewConversationIfNotExists) {
                        String conversationId = save(senderId, recipientId);
                        return Optional.of(conversationId);
                    }

                    return  Optional.empty();
                });
    }

    @Override
    public List<ConversationDTO> findAllByUser(User user) {
        return conversationRepository.findAllBySenderId(user.getUserId()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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

    @Override
    public String save(Long senderId, Long recipientId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        String conversationId = String.format("%s_%s", sender.getUsername(), recipient.getUsername());

        Conversation senderRecipient = Conversation.builder()
                .conversationId(conversationId)
                .senderId(sender.getUserId())
                .recipientId(recipient.getUserId())
                .senderUsername(sender.getUsername())
                .recipientUsername(recipient.getUsername())
                .build();

        Conversation recipientSender = Conversation.builder()
                .conversationId(conversationId)
                .senderId(recipient.getUserId())
                .recipientId(sender.getUserId())
                .senderUsername(recipient.getUsername())
                .recipientUsername(sender.getUsername())
                .build();

        conversationRepository.save(senderRecipient);
        conversationRepository.save(recipientSender);

        return conversationId;
    }

    private ConversationDTO toDTO(Conversation conversation) {
        Optional<ConversationMessage> lastMessageByConversation = conversationMessageRepository.findLastMessageByConversation(conversation.getConversationId());

        return ConversationDTO.builder()
                .id(conversation.getId())
                .lastMessageContent(lastMessageByConversation.map(ConversationMessage::getContent).orElse(""))
                .lastMessageTimestamp(lastMessageByConversation.map(ConversationMessage::getTimestamp).orElse(""))
                .recipientId(conversation.getRecipientId())
                .senderId(conversation.getSenderId())
                .senderUsername(conversation.getSenderUsername())
                .recipientUsername(conversation.getRecipientUsername())
                .conversationId(conversation.getConversationId())
                .build();
    }
}
