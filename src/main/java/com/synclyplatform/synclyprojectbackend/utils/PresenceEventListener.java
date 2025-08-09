package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.security.JwtService;
import com.synclyplatform.synclyprojectbackend.service.user.UserPresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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
        String username = extractUsernameFromEvent(event);
        log.info("Received WebSocket connection event for user {}", username);
        userPresenceService.setOnlineStatus(username);

        messagingTemplate.convertAndSend("/topic/presence", username + " is online.");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String username = extractUsernameFromEvent(event);
        log.info("Received WebSocket disconnection event for user {}", username);
        userPresenceService.setOfflineStatus(username);

        messagingTemplate.convertAndSend("/topic/presence", username  + " is offline.");
    }

    private String extractUsernameFromEvent(AbstractSubProtocolEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String token = Optional.ofNullable(headerAccessor.getFirstNativeHeader("Authorization"))
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7))
                .orElse(null);

        if (token != null) {
            String username = jwtService.extractUsername(token);
            log.info("CONNECTED user: {}", username);
            return username;
        } else {
            log.warn("No token found in CONNECT headers");
        }

        return "anonymous";
    }
}
