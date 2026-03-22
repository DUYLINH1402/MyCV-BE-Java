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
@Schema(description = "Profile update payload (all fields are optional)")
public class ProfileUpdateDTO {

    @Schema(description = "Full name", example = "Nguyen Duy Linh")
    @Size(max = 255, message = "Full name must not exceed 255 characters")
    private String fullName;

    @Schema(description = "Job title / Position", example = "Backend Developer")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Schema(description = "Biography / About me", example = "Passionate about building scalable backend systems...")
    private String bio;

    @Schema(description = "Professional summary - Short text displayed on the homepage", example = "Backend Developer with experience in Java/Spring Boot")
    @Size(max = 500, message = "Professional summary must not exceed 500 characters")
    private String professionalSummary;

    @Schema(description = "Years of experience", example = "3+ Years Experience")
    @Size(max = 50, message = "Experience years must not exceed 50 characters")
    private String experienceYears;

    @Schema(description = "Total number of projects completed", example = "15+ Projects")
    @Size(max = 50, message = "Total projects must not exceed 50 characters")
    private String totalProjects;

    @Schema(description = "Education summary", example = "Computer Science")
    @Size(max = 255, message = "Education summary must not exceed 255 characters")
    private String educationSummary;

    @Schema(description = "Certifications summary", example = "AWS, Spring Professional")
    @Size(max = 255, message = "Certifications summary must not exceed 255 characters")
    private String certSummary;

    @Schema(description = "Contact email address", example = "duylinh@example.com")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Schema(description = "Contact phone number", example = "+84 123 456 789")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @Schema(description = "GitHub profile URL", example = "https://github.com/linhnguyen")
    @Size(max = 255, message = "GitHub URL must not exceed 255 characters")
    private String githubUrl;

    @Schema(description = "LinkedIn profile URL", example = "https://linkedin.com/in/linhnguyen")
    @Size(max = 255, message = "LinkedIn URL must not exceed 255 characters")
    private String linkedinUrl;

    @Schema(description = "Avatar image URL", example = "https://example.com/avatar.jpg")
    @Size(max = 255, message = "Avatar URL must not exceed 255 characters")
    private String avatarUrl;

    @Schema(description = "CV download URL (PDF)", example = "https://example.com/cv/nguyen-duy-linh-cv.pdf")
    @Size(max = 500, message = "CV URL must not exceed 500 characters")
    private String cvUrl;
}
