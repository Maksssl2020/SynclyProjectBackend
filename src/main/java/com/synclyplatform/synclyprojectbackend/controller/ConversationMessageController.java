package com.synclyplatform.synclyprojectbackend.controller;

import com.synclyplatform.synclyprojectbackend.dto.conversation_message.ConversationMessageRequestDTO;
import com.synclyplatform.synclyprojectbackend.dto.conversation_message.ReadMessageRequest;
import com.synclyplatform.synclyprojectbackend.model.conversation_message.ConversationMessage;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.service.conversation_message.ConversationMessageService;
import com.synclyplatform.synclyprojectbackend.utils.ConversationNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        System.out.println("Received message: " + conversationMessageRequest.getMessage()); // Add this
        System.out.println("Received recipient username: " + conversationMessageRequest.getRecipientUsername()); // Add this

        ConversationMessage savedConversationMessage = conversationMessageService.save(conversationMessageRequest);

        System.out.println("Saved conversation message: " + savedConversationMessage.getContent());

        messagingTemplate.convertAndSendToUser(
                savedConversationMessage.getRecipientUsername(),
                "/queue/messages",
                savedConversationMessage
        );

        messagingTemplate.convertAndSendToUser(
                savedConversationMessage.getSenderUsername(),
                "/queue/messages",
                savedConversationMessage
        );

        System.out.println("Saved conversation message: " + savedConversationMessage.getContent());
    }

    @MessageMapping("/conversation/read")
    public void markMessagesAsRead(
            @Payload ReadMessageRequest readMessageRequest,
            Principal principal
    ) {
        conversationMessageService.markAsRead(readMessageRequest.getConversationId(), principal.getName());
    }

    @GetMapping("/by-user-ids/{recipientId}")
    public ResponseEntity<Page<ConversationMessage>> findConversationMessages(
            @AuthenticationPrincipal User user,
            @PathVariable Long recipientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) throws Exception {
        Page<ConversationMessage> conversationMessages = conversationMessageService.findConversationMessages(user, recipientId, page, size);
        conversationMessages.forEach(mess -> System.out.println(mess.getContent()));
        return ResponseEntity.ok(conversationMessages);
    }
}
