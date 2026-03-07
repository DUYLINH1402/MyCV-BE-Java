package com.linhnguyen.portfolio_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình cho Brevo (Sendinblue) Email Service.
 * Đọc các giá trị từ application.yml với prefix "brevo".
 */
@Configuration
@ConfigurationProperties(prefix = "brevo")
@Getter
@Setter
public class BrevoConfig {

    /**
     * API Key của Brevo (lấy từ dashboard Brevo)
     */
    private String apiKey;

    /**
     * Email nhận thông báo (email của chủ portfolio)
     */
    private String recipientEmail;

    /**
     * Tên người nhận (hiển thị trong email)
     */
    private String recipientName;

    /**
     * Email sender (phải được verify trên Brevo)
     */
    private String senderEmail;

    /**
     * Tên sender (hiển thị trong email)
     */
    private String senderName;

    /**
     * Bật/tắt tính năng gửi email
     */
    private boolean enabled = true;
}

