package com.usermgmnt.service.impl;

import com.usermgmnt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${confirmation.expiration.hours}")
    private int confirmationExpirationHours;

    public void sendRegistrationConfirmationEmail(String to, String username) throws MessagingException {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("confirmation:%s".formatted(token), to, confirmationExpirationHours, TimeUnit.HOURS);

        String confirmationUrl = "%s/confirm?token=%s".formatted(baseUrl, token);
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("confirmationUrl", confirmationUrl);

        String emailContent = templateEngine.process("registration-confirmation", context);

        sendHtmlEmail(to, "Confirm Your Registration", emailContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public boolean confirmRegistration(String token) {
        String key = "confirmation:%s".formatted(token);
        String email = redisTemplate.opsForValue().get(key);

        if (email != null) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}