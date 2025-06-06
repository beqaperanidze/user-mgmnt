package com.usermgmnt.service.impl;

import com.usermgmnt.service.EmailService;
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

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${confirmation.expiration.hours}")
    private int confirmationExpirationHours;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, RedisTemplate<String, String> redisTemplate) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.redisTemplate = redisTemplate;
    }

    public void sendRegistrationConfirmationEmail(String to, String username) throws MessagingException {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("confirmation:%s".formatted(token), to, confirmationExpirationHours, TimeUnit.HOURS);

        String confirmationUrl = "%s/auth/confirm?token=%s".formatted(baseUrl, token);
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("confirmationUrl", confirmationUrl);

        String emailContent = templateEngine.process("registration-confirmation", context);

        sendHtmlEmail(to, "Confirm Your Registration", emailContent);
    }

    public void sendWelcomeEmail(String to, String username) throws MessagingException {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("loginUrl", "%s/auth/login".formatted(baseUrl));

        String emailContent = templateEngine.process("welcome-email", context);

        sendHtmlEmail(to, "Welcome to Our Platform!", emailContent);
    }

    public void sendPasswordResetEmail(String to, String username) throws MessagingException {
        String token = generateVerificationCode();

        redisTemplate.opsForValue().set("password-reset:%s".formatted(to), token, 1, TimeUnit.HOURS);

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("token", token);
        context.setVariable("expirationHours", 1);

        String emailContent = templateEngine.process("password-reset", context);

        sendHtmlEmail(to, "Reset Your Password", emailContent);
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

    private String generateVerificationCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }
}