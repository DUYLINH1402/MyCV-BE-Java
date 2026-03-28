package com.linhnguyen.portfolio_api.profile.dto;

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
@Schema(description = "Portfolio owner's profile information")
public class ProfileResponseDTO {

    @Schema(description = "Profile ID", example = "1")
    private Long id;

    @Schema(description = "Full name", example = "Nguyen Duy Linh")
    private String fullName;

    @Schema(description = "Job title / Position", example = "Backend Developer")
    private String title;

    @Schema(description = "Biography / About me", example = "Passionate about building scalable backend systems...")
    private String bio;

    @Schema(description = "Professional summary - Short text displayed on the homepage", example = "Backend Developer with experience in Java/Spring Boot")
    private String professionalSummary;

    @Schema(description = "Years of experience", example = "3+ Years Experience")
    private String experienceYears;

    @Schema(description = "Total number of projects completed", example = "15+ Projects")
    private String totalProjects;

    @Schema(description = "Education summary", example = "Computer Science")
    private String educationSummary;

    @Schema(description = "Certifications summary", example = "AWS, Spring Professional")
    private String certSummary;

    @Schema(description = "Contact email address", example = "duylinh@example.com")
    private String email;

    @Schema(description = "Contact phone number", example = "+84 123 456 789")
    private String phoneNumber;

    @Schema(description = "GitHub profile URL", example = "https://github.com/linhnguyen")
    private String githubUrl;

    @Schema(description = "LinkedIn profile URL", example = "https://linkedin.com/in/linhnguyen")
    private String linkedinUrl;

    @Schema(description = "Avatar image URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "CV download URL (PDF)", example = "https://example.com/cv/nguyen-duy-linh-cv.pdf")
    private String cvUrl;

    @Schema(description = "Record creation timestamp", example = "2025-01-11T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated timestamp", example = "2025-01-11T15:45:00")
    private LocalDateTime updatedAt;
}

