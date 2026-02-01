package com.synclyplatform.synclyprojectbackend.service.email_sender;

import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface EmailSenderService {

    void sendTwoFactorCodeEmail(String to, String username, String code, String subject) throws MessagingException;
}
