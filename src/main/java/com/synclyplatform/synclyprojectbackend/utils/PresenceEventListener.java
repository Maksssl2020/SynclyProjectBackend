package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.security.JwtService;
import com.synclyplatform.synclyprojectbackend.service.user.UserPresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PresenceEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserPresenceService userPresenceService;
    private final JwtService jwtService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = extractUsernameFromConnect(headerAccessor);

        if (username == null) {
            log.warn("Cannot extract username from WebSocket CONNECT");
            return;
        }

        headerAccessor.getSessionAttributes().put("username", username);

        log.info("WebSocket connected: {}", username);

        userPresenceService.setOnlineStatus(username);
        messagingTemplate.convertAndSend("/topic/presence", username + " is online.");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = Optional.ofNullable(headerAccessor.getSessionAttributes())
                .map(attrs -> (String) attrs.get("username"))
                .orElse(null);

        if (username == null) {
            log.warn("Cannot extract username from WebSocket DISCONNECT");
            return;
        }

        log.info("WebSocket disconnected: {}", username);

        userPresenceService.setOfflineStatus(username);
        messagingTemplate.convertAndSend("/topic/presence", username + " is offline.");
    }

    @MessageMapping("/presence")
    public void updatePresence(@Payload PresenceRequest request, Principal principal) {
        String username = principal.getName();

        if ("offline".equals(request.getStatus())) {
            userPresenceService.setOfflineStatus(username);
            messagingTemplate.convertAndSend("/topic/presence", username + " is offline.");
        }

        if ("online".equals(request.getStatus())) {
            userPresenceService.setOnlineStatus(username);
            messagingTemplate.convertAndSend("/topic/presence", username + " is online.");
        }
    }

    private String extractUsernameFromConnect(StompHeaderAccessor headerAccessor) {
        String token = Optional.ofNullable(headerAccessor.getFirstNativeHeader("Authorization"))
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7))
                .orElse(null);

        if (token == null) {
            return null;
        }

        return jwtService.extractUsername(token);
    }
}
