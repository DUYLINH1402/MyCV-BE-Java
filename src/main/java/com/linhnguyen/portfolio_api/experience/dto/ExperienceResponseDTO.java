package com.linhnguyen.portfolio_api.experience.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO chứa dữ liệu Professional Experience trả về cho client.
 */
@Getter
@Builder
@Schema(description = "Detailed professional experience information")
public class ExperienceResponseDTO {

    @Schema(description = "Experience ID", example = "1")
    private Long id;

    @Schema(description = "Job title / position", example = "Backend Developer")
    private String jobTitle;

    @Schema(description = "Company / organization name", example = "FPT Software")
    private String company;

    @Schema(description = "Company logo URL (displayed on timeline)", example = "https://example.com/logos/fpt-software.png")
    private String companyLogo;

    @Schema(description = "Work location (city, country)", example = "Ho Chi Minh City, Vietnam")
    private String location;

    @Schema(description = "Employment start date", example = "2024-01-15")
    private LocalDate startDate;

    @Schema(description = "Employment end date (null if currently working)", example = "2025-06-30")
    private LocalDate endDate;

    @Schema(description = "Job responsibilities and achievements as bullet points",
            example = "[\"Developed REST APIs with Spring Boot\", \"Optimized database queries reducing response time by 40%\"]")
    private List<String> description;

    @Schema(description = "Record creation timestamp", example = "2025-01-11T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated timestamp", example = "2025-01-11T15:45:00")
    private LocalDateTime updatedAt;
}

