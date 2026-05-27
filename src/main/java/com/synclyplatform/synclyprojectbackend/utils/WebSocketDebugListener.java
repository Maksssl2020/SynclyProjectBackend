package com.synclyplatform.synclyprojectbackend.utils;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@AllArgsConstructor
public class WebSocketDebugListener {

    private final SimpUserRegistry simpUserRegistry;

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        System.out.println("=== SUBSCRIBE EVENT ===");
        System.out.println("User: " + accessor.getUser());
        System.out.println("Destination: " + accessor.getDestination());

        simpUserRegistry.getUsers().forEach(user -> {
            System.out.println("User: " + user.getName());
            user.getSessions().forEach(session -> {
                System.out.println("Session: " + session.getId());
                session.getSubscriptions().forEach(sub -> {
                    System.out.println("Sub: " + sub.getDestination());
                });
            });
        });

        System.out.println("=======================");
    }
}
