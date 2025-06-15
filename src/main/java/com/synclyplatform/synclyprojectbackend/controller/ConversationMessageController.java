package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.conversation_message.ConversationMessageRequestDTO;
import com.synclyplatform.synclyprojectbackend.model.conversation_message.ConversationMessage;
import com.synclyplatform.synclyprojectbackend.service.conversation_message.ConversationMessageService;
import com.synclyplatform.synclyprojectbackend.utils.ConversationNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversation/messages")
public class ConversationMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ConversationMessageService conversationMessageService;

    @MessageMapping("/conversation")
    public void processMessage(
            @Payload ConversationMessageRequestDTO conversationMessageRequest
    ) throws Exception {
        ConversationMessage savedConversationMessage = conversationMessageService.save(conversationMessageRequest);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(savedConversationMessage.getRecipientUserId()),
                "/queue/messages",
                ConversationNotification.builder()
                        .conversationId(savedConversationMessage.getConversationId())
                        .recipientUserId(savedConversationMessage.getRecipientUserId())
                        .senderUserId(savedConversationMessage.getSenderUserId())
                        .senderUsername(savedConversationMessage.getSenderUsername())
                        .recipientUsername(savedConversationMessage.getRecipientUsername())
                        .messageContent(savedConversationMessage.getContent())
                        .build()
        );
    }

    @GetMapping("/{senderUsername}/{recipientUsername}")
    public ResponseEntity<List<ConversationMessage>> findConversationMessages(
            @PathVariable String senderUsername,
            @PathVariable String recipientUsername
    ) throws Exception {
        return ResponseEntity.ok(conversationMessageService.findConversationMessages(senderUsername, recipientUsername));
    }
}
