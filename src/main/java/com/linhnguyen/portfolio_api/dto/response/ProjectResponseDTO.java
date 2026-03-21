package com.linhnguyen.portfolio_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO chứa dữ liệu Project trả về cho client.
 * Chỉ bao gồm các thông tin cần thiết cho việc hiển thị dự án.
 */
@Getter
@Builder
@Schema(description = "Detailed project information")
public class ProjectResponseDTO {

    @Schema(description = "Project ID", example = "1")
    private Long id;

    @Schema(description = "Project title", example = "Portfolio API")
    private String title;

    @Schema(description = "Short description of the project (displayed in list view)", example = "RESTful API for Personal Portfolio")
    private String shortDescription;

    @Schema(description = "Full detailed description of the project (JSON format). Contains complex structures like: sections, bullet points, highlights...")
    private Map<String, Object> fullDescription;

    @Schema(description = "Thumbnail image URL", example = "https://example.com/project-image.jpg")
    private String imageUrl;

    @Schema(description = "Live demo URL", example = "https://demo.example.com")
    private String demoUrl;

    @Schema(description = "GitHub repository URL", example = "https://github.com/linhnguyen/portfolio-api")
    private String githubUrl;

    @Schema(description = "Project review/blog post URL", example = "https://blog.example.com/portfolio-api")
    private String reviewUrl;

    @Schema(description = "Project demo/introduction video URL (YouTube, Vimeo, etc.)", example = "https://youtube.com/watch?v=abc123")
    private String videoUrl;

    @Schema(description = "Project gallery images (screenshots, demos, etc.)", example = "[\"https://example.com/img1.jpg\", \"https://example.com/img2.jpg\"]")
    private List<String> gallery;

    @Schema(description = "Technologies used in the project", example = "[\"Java\", \"Spring Boot\", \"PostgreSQL\"]")
    private List<String> technologies;

    @Schema(description = "Project category", example = "Backend", allowableValues = {"Web", "Mobile", "Backend", "Fullstack"})
    private String category;

    @Schema(description = "Whether the project is marked as featured", example = "true")
    private Boolean isFeatured;

    @Schema(description = "Display order", example = "1")
    private Integer displayOrder;

    @Schema(description = "Project status", example = "completed", allowableValues = {"completed", "in_progress", "archived"})
    private String status;

    @Schema(description = "Project completion/start date", example = "2025-01-01")
    private LocalDate projectDate;

    @Schema(description = "Record creation timestamp", example = "2025-01-11T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated timestamp", example = "2025-01-11T15:45:00")
    private LocalDateTime updatedAt;
}
