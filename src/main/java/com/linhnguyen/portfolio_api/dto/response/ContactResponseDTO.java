package com.linhnguyen.portfolio_api.dto.response;

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
@Schema(description = "Response sau khi gửi tin nhắn liên hệ")
public class ContactResponseDTO {

    @Schema(description = "ID của tin nhắn", example = "1")
    private Long id;

    @Schema(description = "Tên người gửi", example = "Nguyễn Văn A")
    private String senderName;

    @Schema(description = "Email người gửi", example = "recruiter@company.com")
    private String senderEmail;

    @Schema(description = "Tiêu đề tin nhắn", example = "Lời mời phỏng vấn")
    private String subject;

    @Schema(description = "Thời điểm gửi tin nhắn", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Thông báo xác nhận", example = "Tin nhắn của bạn đã được gửi thành công!")
    private String confirmationMessage;
}

