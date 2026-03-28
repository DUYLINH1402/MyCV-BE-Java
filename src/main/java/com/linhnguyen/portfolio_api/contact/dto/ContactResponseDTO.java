package com.linhnguyen.portfolio_api.contact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO response sau khi gửi tin nhắn liên hệ thành công.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response after sending a contact message")
public class ContactResponseDTO {

    @Schema(description = "Message ID", example = "1")
    private Long id;

    @Schema(description = "Sender's name", example = "John Smith")
    private String senderName;

    @Schema(description = "Sender's email", example = "recruiter@company.com")
    private String senderEmail;

    @Schema(description = "Message subject", example = "Interview Invitation")
    private String subject;

    @Schema(description = "Timestamp when the message was sent", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Confirmation message", example = "Your message has been sent successfully!")
    private String confirmationMessage;
}

