package com.linhnguyen.portfolio_api.experience.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO chứa dữ liệu để cập nhật Professional Experience.
 * Tất cả các trường đều optional.
 */
@Getter
@Setter
@Builder
@Schema(description = "Professional experience update payload (all fields are optional)")
public class ExperienceUpdateDTO {

    @Schema(description = "Job title / position", example = "Senior Backend Developer")
    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String jobTitle;

    @Schema(description = "Company / organization name", example = "FPT Software")
    @Size(max = 255, message = "Company must not exceed 255 characters")
    private String company;

    @Schema(description = "Company logo URL (displayed on timeline)", example = "https://example.com/logos/fpt-software.png")
    @Size(max = 500, message = "Company logo URL must not exceed 500 characters")
    private String companyLogo;

    @Schema(description = "Work location (city, country)", example = "Ho Chi Minh City, Vietnam")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Schema(description = "Employment start date", example = "2024-01-15")
    private LocalDate startDate;

    @Schema(description = "Employment end date (null if currently working)", example = "2025-06-30")
    private LocalDate endDate;

    @Schema(description = "Job responsibilities and achievements as bullet points",
            example = "[\"Developed REST APIs with Spring Boot\", \"Optimized database queries reducing response time by 40%\"]")
    private List<String> description;
}

