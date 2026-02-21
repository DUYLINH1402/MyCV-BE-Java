package com.linhnguyen.portfolio_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * DTO chứa dữ liệu Profile trả về cho client.
 * Chỉ bao gồm các thông tin cần thiết, ẩn các trường nhạy cảm và internal fields.
 */
@Getter
@Builder
@Schema(description = "Thông tin profile của chủ sở hữu Portfolio")
public class ProfileResponseDTO {

    @Schema(description = "ID của profile", example = "1")
    private Long id;

    @Schema(description = "Họ và tên đầy đủ", example = "Nguyễn Duy Linh")
    private String fullName;

    @Schema(description = "Chức danh / Vị trí công việc", example = "Backend Developer")
    private String title;

    @Schema(description = "Tiểu sử / Giới thiệu bản thân", example = "Passionate about building scalable backend systems...")
    private String bio;

    @Schema(description = "Tóm tắt chuyên môn - Đoạn text ngắn hiển thị ở trang chủ", example = "Backend Developer với kinh nghiệm về Java/Spring Boot")
    private String professionalSummary;

    @Schema(description = "Số năm kinh nghiệm", example = "3+ Years Experience")
    private String experienceYears;

    @Schema(description = "Tổng số dự án đã thực hiện", example = "15+ Projects")
    private String totalProjects;

    @Schema(description = "Tóm tắt học vấn", example = "Computer Science")
    private String educationSummary;

    @Schema(description = "Tóm tắt chứng chỉ", example = "AWS, Spring Professional")
    private String certSummary;

    @Schema(description = "Địa chỉ email liên hệ", example = "duylinh@example.com")
    private String email;

    @Schema(description = "Số điện thoại liên hệ", example = "+84 123 456 789")
    private String phoneNumber;

    @Schema(description = "Đường dẫn đến trang GitHub", example = "https://github.com/linhnguyen")
    private String githubUrl;

    @Schema(description = "Đường dẫn đến trang LinkedIn", example = "https://linkedin.com/in/linhnguyen")
    private String linkedinUrl;

    @Schema(description = "Đường dẫn đến ảnh đại diện", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Thời điểm tạo bản ghi", example = "2025-01-11T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Thời điểm cập nhật gần nhất", example = "2025-01-11T15:45:00")
    private LocalDateTime updatedAt;
}

