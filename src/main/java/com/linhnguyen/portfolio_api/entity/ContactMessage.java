package com.linhnguyen.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity lưu trữ các tin nhắn liên hệ từ nhà tuyển dụng.
 * Kế thừa BaseEntity để có đầy đủ audit fields (createdAt, updatedAt, etc.).
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

    /**
     * Tên người gửi
     */
    @Column(name = "sender_name", nullable = false, length = 100)
    private String senderName;

    /**
     * Email người gửi để phản hồi
     */
    @Column(name = "sender_email", nullable = false, length = 255)
    private String senderEmail;

    /**
     * Tiêu đề tin nhắn
     */
    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    /**
     * Nội dung tin nhắn
     */
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    /**
     * Trạng thái đã đọc hay chưa
     */
    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;

    /**
     * Trạng thái email đã gửi thành công hay chưa
     */
    @Column(name = "email_sent")
    @Builder.Default
    private Boolean emailSent = false;

    /**
     * IP address của người gửi (để tracking spam)
     */
    @Column(name = "sender_ip", length = 45)
    private String senderIp;
}


