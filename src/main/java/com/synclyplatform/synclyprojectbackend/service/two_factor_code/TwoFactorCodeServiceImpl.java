package com.synclyplatform.synclyprojectbackend.service.two_factor_code;

import com.synclyplatform.synclyprojectbackend.model.two_factor_code.TwoFactorCode;
import com.synclyplatform.synclyprojectbackend.model.user.User;
import com.synclyplatform.synclyprojectbackend.repository.TwoFactorCodeRepository;
import com.synclyplatform.synclyprojectbackend.repository.UserRepository;
import com.synclyplatform.synclyprojectbackend.service.email_sender.EmailSenderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class TwoFactorCodeServiceImpl implements TwoFactorCodeService {

    private final TwoFactorCodeRepository twoFactorCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    @Override
    @Transactional
    public void generateTwoFactorCode(Long userId, String emailAddress) throws Exception {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found, id: " + userId));

        String code = String.format("%06d", (int) (Math.random() * 999999));
        String hashedCode = passwordEncoder.encode(code);

        ZonedDateTime createdAt = ZonedDateTime.now();
        ZonedDateTime expiresAt = createdAt.plusMinutes(10);

        twoFactorCodeRepository.deleteByUserId(userId);
        TwoFactorCode twoFactorCode = TwoFactorCode.builder()
                .userId(userId)
                .code(hashedCode)
                .createdAt(createdAt)
                .expiresAt(expiresAt)
                .build();

        twoFactorCodeRepository.save(twoFactorCode);

        emailSenderService.sendTwoFactorCodeEmail(emailAddress, foundUser.getUsername(), code, "Syncly Authorization Code");
    }

    @Override
    public boolean verifyTwoFactorCode(Long userId, String code) {
        return twoFactorCodeRepository.findByUserId(userId)
                .filter(token -> token.getExpiresAt().isAfter(ZonedDateTime.now()))
                .map(token -> passwordEncoder.matches(code, token.getCode()))
                .orElse(false);
    }
}
