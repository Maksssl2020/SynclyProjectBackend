package com.synclyplatform.synclyprojectbackend.service.login_history;

import com.synclyplatform.synclyprojectbackend.dto.login_history.LoginHistoryDTO;
import com.synclyplatform.synclyprojectbackend.mapper.LoginHistoryMapper;
import com.synclyplatform.synclyprojectbackend.model.login_history.LoginHistory;
import com.synclyplatform.synclyprojectbackend.repository.LoginHistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;
    private final LoginHistoryMapper loginHistoryMapper;

    @Override
    public void saveLoginHistory(Long userId, HttpServletRequest request) {
        String ip = extractClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        String city = "Unknown", country = "Unknown", timezone = "UTC";
        ZoneId zoneId = ZoneId.of("UTC");

        try {
            String url = String.format("https://ipapi.co/%s/json/", ip);
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> location = restTemplate.getForObject(url, Map.class);

            if (location != null) {
                city = (String) location.getOrDefault("city", "Unknown");
                country = (String) location.getOrDefault("country_name", "Unknown");
                timezone = (String) location.getOrDefault("timezone", "UTC");

                try {
                    zoneId = ZoneId.of(timezone);
                }  catch (Exception e) {
                    zoneId = ZoneId.of("UTC");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ZonedDateTime timestamp = ZonedDateTime.now(zoneId);

        LoginHistory loginHistory = LoginHistory.builder()
                .userId(userId)
                .city(city)
                .country(country)
                .ip(ip)
                .userAgent(userAgent)
                .timestamp(timestamp)
                .build();

        loginHistoryRepository.save(loginHistory);
    }

    @Override
    public List<LoginHistoryDTO> findLoginHistoryByUserId(Long userId) {
        return loginHistoryRepository.findByUserIdOrderByTimestampDesc(userId).stream()
                .map(loginHistoryMapper::loginHistoryToLoginHistoryDTO)
                .collect(Collectors.toList());
    }

    private String extractClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return xfHeader == null ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }
}
