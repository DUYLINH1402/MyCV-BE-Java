package com.linhnguyen.portfolio_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO chứa dữ liệu để cập nhật Profile.
 * Tất cả các trường đều optional - chỉ cập nhật những trường được gửi lên.
 */
@Getter
@Setter
@Builder
@Schema(description = "Thông tin cập nhật Profile (tất cả trường đều optional)")
public class ProfileUpdateDTO {

    @Schema(description = "Họ và tên đầy đủ", example = "Nguyễn Duy Linh")
    @Size(max = 255, message = "Họ tên không được vượt quá 255 ký tự")
    private String fullName;

    @Schema(description = "Chức danh / Vị trí công việc", example = "Backend Developer")
    @Size(max = 255, message = "Chức danh không được vượt quá 255 ký tự")
    private String title;

    @Schema(description = "Tiểu sử / Giới thiệu bản thân", example = "Passionate about building scalable backend systems...")
    private String bio;

    @Schema(description = "Tóm tắt chuyên môn - Đoạn text ngắn hiển thị ở trang chủ", example = "Backend Developer với kinh nghiệm về Java/Spring Boot")
    @Size(max = 500, message = "Tóm tắt chuyên môn không được vượt quá 500 ký tự")
    private String professionalSummary;

    @Schema(description = "Số năm kinh nghiệm", example = "3+ Years Experience")
    @Size(max = 50, message = "Số năm kinh nghiệm không được vượt quá 50 ký tự")
    private String experienceYears;

    @Schema(description = "Tổng số dự án đã thực hiện", example = "15+ Projects")
    @Size(max = 50, message = "Tổng số dự án không được vượt quá 50 ký tự")
    private String totalProjects;

    @Schema(description = "Tóm tắt học vấn", example = "Computer Science")
    @Size(max = 255, message = "Tóm tắt học vấn không được vượt quá 255 ký tự")
    private String educationSummary;

    @Schema(description = "Tóm tắt chứng chỉ", example = "AWS, Spring Professional")
    @Size(max = 255, message = "Tóm tắt chứng chỉ không được vượt quá 255 ký tự")
    private String certSummary;

    @Schema(description = "Địa chỉ email liên hệ", example = "duylinh@example.com")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;

    @Schema(description = "Số điện thoại liên hệ", example = "+84 123 456 789")
    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    private String phoneNumber;

    @Schema(description = "Đường dẫn đến trang GitHub", example = "https://github.com/linhnguyen")
    @Size(max = 255, message = "GitHub URL không được vượt quá 255 ký tự")
    private String githubUrl;

    @Schema(description = "Đường dẫn đến trang LinkedIn", example = "https://linkedin.com/in/linhnguyen")
    @Size(max = 255, message = "LinkedIn URL không được vượt quá 255 ký tự")
    private String linkedinUrl;

    @Schema(description = "Đường dẫn đến ảnh đại diện", example = "https://example.com/avatar.jpg")
    @Size(max = 255, message = "Avatar URL không được vượt quá 255 ký tự")
    private String avatarUrl;
}

