package com.linhnguyen.portfolio_api.contact.entity;

import com.linhnguyen.portfolio_api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity lưu trữ các tin nhắn liên hệ từ nhà tuyển dụng.
 */
@Entity
@Table(name = "contact_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_name", nullable = false, length = 100)
    private String senderName;

    @Column(name = "sender_email", nullable = false, length = 255)
    private String senderEmail;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "email_sent")
    @Builder.Default
    private Boolean emailSent = false;

    @Column(name = "sender_ip", length = 45)
    private String senderIp;
}

