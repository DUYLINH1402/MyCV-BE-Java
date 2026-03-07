package com.linhnguyen.portfolio_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO cho request gửi tin nhắn liên hệ.
 * Validate đầy đủ các trường để đảm bảo dữ liệu hợp lệ.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request gửi tin nhắn liên hệ từ nhà tuyển dụng")
public class ContactRequestDTO {

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 100, message = "Tên phải từ 2-100 ký tự")
    @Schema(
            description = "Tên người gửi",
            example = "Nguyễn Văn A",
            minLength = 2,
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email không được quá 255 ký tự")
    @Schema(
            description = "Email người gửi để liên hệ lại",
            example = "recruiter@company.com",
            maxLength = 255,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 5, max = 255, message = "Tiêu đề phải từ 5-255 ký tự")
    @Schema(
            description = "Tiêu đề tin nhắn",
            example = "Lời mời phỏng vấn vị trí Backend Developer",
            minLength = 5,
            maxLength = 255,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String subject;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    @Size(min = 10, max = 5000, message = "Nội dung phải từ 10-5000 ký tự")
    @Schema(
            description = "Nội dung tin nhắn chi tiết",
            example = "Chào Linh, chúng tôi đang tìm kiếm Backend Developer và profile của bạn rất phù hợp...",
            minLength = 10,
            maxLength = 5000,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String message;
}

