package ch.bbw.pr.tresorbackend.service.impl;

import ch.bbw.pr.tresorbackend.service.ReCaptchaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Service
@Component
public class ReCaptchaServiceImpl implements ReCaptchaService {
    private static final Logger logger = LoggerFactory.getLogger(ReCaptchaServiceImpl.class);
    private final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    @Override
    public boolean verifyCaptcha(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", recaptchaSecret);
            params.add("response", token);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(RECAPTCHA_VERIFY_URL, request, Map.class);
            Map<String, Object> response = responseEntity.getBody();

            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                Double score = (Double) response.get("score");
                String action = (String) response.get("action");

                logger.info("ReCaptcha score: " + score + ", action: " + action);

                return score != null && score >= 0.5 && "register".equals(action);
            }
        } catch (Exception e) {
            logger.error("ReCaptcha verification failed: " + e.getMessage());
        }

        return false;
    }

}
