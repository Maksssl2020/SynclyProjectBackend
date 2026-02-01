package com.synclyplatform.synclyprojectbackend.service.email_sender;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final String ADMIN_EMAIL = "skomunikacja.software@gmail.com";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Override
    public void sendTwoFactorCodeEmail(String to, String username, String code, String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(ADMIN_EMAIL);
            mimeMessageHelper.setSubject(subject);

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("code", code);

            String templateContent = springTemplateEngine.process("two-factor-verification-code-email-template", context);
            mimeMessageHelper.setText(templateContent, true);
            mailSender.send(mimeMessage);
        } catch (Exception exception) {
            throw new IllegalStateException("Nie udało się wysłać wiadomości e-mail 2FA.");
        }
    }
}
