package com.synclyplatform.synclyprojectbackend.utils;

import com.synclyplatform.synclyprojectbackend.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class StompListener implements ApplicationListener<SessionConnectEvent> {

    private final JwtService jwtService;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String token = Optional.ofNullable(headerAccessor.getFirstNativeHeader("Authorization"))
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7))
                .orElse(null);

        if (token != null) {
            String username = jwtService.extractUsername(token);
            log.info("CONNECTED user: {}", username);
        } else {
            log.warn("No token found in CONNECT headers");
        }
    }
}
