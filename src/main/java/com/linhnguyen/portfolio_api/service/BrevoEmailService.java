package com.linhnguyen.portfolio_api.service;

import com.linhnguyen.portfolio_api.config.BrevoConfig;
import com.linhnguyen.portfolio_api.entity.ContactMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service gửi email qua Brevo (Sendinblue) API.
 * Sử dụng REST API v3 của Brevo để gửi email transactional.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BrevoEmailService {

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";
    private static final String CONTACT_TEMPLATE_PATH = "templates/email/contact-notification.html";

    private final BrevoConfig brevoConfig;
    private final RestTemplate restTemplate;

    /** Template HTML được cache khi khởi động */
    private String contactNotificationTemplate;

    /**
     * Load template từ resources khi khởi động.
     * Cache lại để không phải đọc file mỗi lần gửi email.
     */
    @PostConstruct
    public void init() {
        // Validate config - detect khi biến môi trường không được resolve
        if (brevoConfig.isEnabled()) {
            String senderEmail = brevoConfig.getSenderEmail();
            String recipientEmail = brevoConfig.getRecipientEmail();

            // Kiểm tra nếu giá trị vẫn còn dạng ${...} (không được resolve)
            if (senderEmail == null || senderEmail.isBlank() || senderEmail.startsWith("${")) {
                log.error("BREVO_SENDER_EMAIL không được cấu hình đúng! Giá trị hiện tại: {}", senderEmail);
            }
            if (recipientEmail == null || recipientEmail.isBlank() || recipientEmail.startsWith("${")) {
                log.error("BREVO_RECIPIENT_EMAIL không được cấu hình đúng! Giá trị hiện tại: {}", recipientEmail);
            }
            if (brevoConfig.getApiKey() == null || brevoConfig.getApiKey().isBlank()) {
                log.error("BREVO_API_KEY chưa được cấu hình! Email sẽ không thể gửi được.");
            }
        }

        // Load template
        try {
            ClassPathResource resource = new ClassPathResource(CONTACT_TEMPLATE_PATH);
            contactNotificationTemplate = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            log.info("Đã load email template: {}", CONTACT_TEMPLATE_PATH);
        } catch (IOException e) {
            log.error("Không thể load email template: {}. Chi tiết: {}", CONTACT_TEMPLATE_PATH, e.getMessage());
            // Fallback template nếu không load được file
            contactNotificationTemplate = getDefaultTemplate();
        }
    }

    /**
     * Gửi email thông báo tin nhắn liên hệ mới (async).
     * Chạy bất đồng bộ để không block request của user.
     *
     * @param contactMessage Tin nhắn liên hệ cần gửi thông báo
     */
    @Async
    public void sendContactNotificationAsync(ContactMessage contactMessage) {
        if (!brevoConfig.isEnabled()) {
            log.warn("Brevo email service is disabled. Skipping email for message ID: {}", contactMessage.getId());
            return;
        }

        try {
            boolean success = sendContactNotification(contactMessage);
            if (success) {
                log.info("Successfully sent email notification for message ID: {}", contactMessage.getId());
            } else {
                log.error("Failed to send email notification for message ID: {}", contactMessage.getId());
            }
        } catch (Exception e) {
            log.error("Error sending email for message ID: {}. Detail: {}", contactMessage.getId(), e.getMessage());
        }
    }

    /**
     * Gửi email thông báo tin nhắn liên hệ mới (sync).
     *
     * @param contactMessage Tin nhắn liên hệ
     * @return true nếu gửi thành công
     */
    public boolean sendContactNotification(ContactMessage contactMessage) {
        if (!brevoConfig.isEnabled()) {
            log.warn("Brevo email service bị tắt");
            return false;
        }

        try {
            HttpHeaders headers = createHeaders();
            Map<String, Object> requestBody = buildEmailPayload(contactMessage);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    BREVO_API_URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email sent successfully via Brevo. Message ID: {}", contactMessage.getId());
                return true;
            } else {
                log.error("Brevo API failed. Status: {}, Response: {}",
                        response.getStatusCode(), response.getBody());
                return false;
            }

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Brevo API error - Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            log.error("Error calling Brevo API: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Tạo HTTP headers với API key authentication.
     * Brevo API v3 yêu cầu header "api-key" với giá trị là API key.
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        String apiKey = brevoConfig.getApiKey();
        if (apiKey == null || apiKey.isBlank()) {
            log.error("Brevo API key is null or blank!");
        } else {
            // Log prefix để debug (không log toàn bộ key vì lý do bảo mật)
            log.debug("Using API key starting with: {}...", apiKey.substring(0, Math.min(15, apiKey.length())));
        }

        headers.set("api-key", apiKey);
        return headers;
    }

    /**
     * Xây dựng payload email theo format của Brevo API.
     *
     * @param contactMessage Tin nhắn liên hệ
     * @return Map chứa payload
     */
    private Map<String, Object> buildEmailPayload(ContactMessage contactMessage) {
        Map<String, Object> payload = new HashMap<>();

        // Sender (email đã verify trên Brevo)
        String senderEmail = brevoConfig.getSenderEmail().trim();
        String senderName = brevoConfig.getSenderName().trim();

        log.info("Building payload with sender - Email: '{}', Name: '{}'", senderEmail, senderName);

        Map<String, String> sender = new HashMap<>();
        sender.put("name", senderName);
        sender.put("email", senderEmail);
        payload.put("sender", sender);

        // Recipient (email của chủ portfolio)
        String recipientEmail = brevoConfig.getRecipientEmail().trim();
        String recipientName = brevoConfig.getRecipientName().trim();

        Map<String, String> recipient = new HashMap<>();
        recipient.put("name", recipientName);
        recipient.put("email", recipientEmail);
        payload.put("to", List.of(recipient));

        // Reply-To (email của người gửi tin nhắn)
        Map<String, String> replyTo = new HashMap<>();
        replyTo.put("name", contactMessage.getSenderName());
        replyTo.put("email", contactMessage.getSenderEmail());
        payload.put("replyTo", replyTo);

        // Subject
        payload.put("subject", "[Portfolio Contact] " + contactMessage.getSubject());

        // HTML Content
        payload.put("htmlContent", buildHtmlContent(contactMessage));

        return payload;
    }

    /**
     * Xây dựng nội dung HTML cho email từ template.
     * Thay thế các placeholder bằng dữ liệu thực tế.
     *
     * @param contactMessage Tin nhắn liên hệ
     * @return HTML content string
     */
    private String buildHtmlContent(ContactMessage contactMessage) {
        return contactNotificationTemplate
                .replace("{{senderName}}", escapeHtml(contactMessage.getSenderName()))
                .replace("{{senderEmail}}", escapeHtml(contactMessage.getSenderEmail()))
                .replace("{{subject}}", escapeHtml(contactMessage.getSubject()))
                .replace("{{message}}", escapeHtml(contactMessage.getMessage()).replace("\n", "<br>"));
    }

    /**
     * Template mặc định nếu không load được từ file.
     * Đảm bảo service vẫn hoạt động khi có lỗi IO.
     */
    private String getDefaultTemplate() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 8px 8px 0 0; }
                        .content { background: #f9f9f9; padding: 20px; border: 1px solid #ddd; border-top: none; }
                        .info-row { margin: 10px 0; padding: 10px; background: white; border-radius: 4px; }
                        .label { font-weight: bold; color: #667eea; }
                        .message-box { background: white; padding: 15px; border-left: 4px solid #667eea; margin-top: 15px; }
                        .footer { text-align: center; padding: 15px; color: #888; font-size: 12px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h2>📬 New Contact Message from Portfolio</h2>
                        </div>
                        <div class="content">
                            <div class="info-row">
                                <span class="label">👤 Sender:</span> {{senderName}}
                            </div>
                            <div class="info-row">
                                <span class="label">📧 Email:</span> <a href="mailto:{{senderEmail}}">{{senderEmail}}</a>
                            </div>
                            <div class="info-row">
                                <span class="label">📌 Subject:</span> {{subject}}
                            </div>
                            <div class="message-box">
                                <span class="label">💬 Message:</span>
                                <p>{{message}}</p>
                            </div>
                        </div>
                        <div class="footer">
                            <p>This email was sent automatically from Nguyen Duy Linh's Portfolio</p>
                            <p>You can reply directly to this email to respond to the sender.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;
    }

    /**
     * Escape HTML để tránh XSS trong email.
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}

