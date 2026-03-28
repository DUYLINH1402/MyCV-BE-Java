package com.linhnguyen.portfolio_api.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO chứa dữ liệu để cập nhật Project.
 * Tất cả các trường đều optional.
 */
@Getter
@Setter
@Builder
@Schema(description = "Project update payload (all fields are optional)")
public class ProjectUpdateDTO {

    @Schema(description = "Project title", example = "Portfolio API")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Schema(description = "Short description of the project (displayed in list view)", example = "RESTful API for Personal Portfolio")
    @Size(max = 500, message = "Short description must not exceed 500 characters")
    private String shortDescription;

    @Schema(description = "Full detailed description of the project (JSON format)")
    private Map<String, Object> fullDescription;

    @Schema(description = "Thumbnail image URL", example = "https://example.com/project-image.jpg")
    @Size(max = 255, message = "Image URL must not exceed 255 characters")
    private String imageUrl;

    @Schema(description = "Live demo URL", example = "https://demo.example.com")
    @Size(max = 255, message = "Demo URL must not exceed 255 characters")
    private String demoUrl;

    @Schema(description = "GitHub repository URL", example = "https://github.com/linhnguyen/portfolio-api")
    @Size(max = 255, message = "GitHub URL must not exceed 255 characters")
    private String githubUrl;

    @Schema(description = "Project review/blog post URL", example = "https://blog.example.com/portfolio-api")
    @Size(max = 500, message = "Review URL must not exceed 500 characters")
    private String reviewUrl;

    @Schema(description = "Project demo/introduction video URL (YouTube, Vimeo, etc.)", example = "https://youtube.com/watch?v=abc123")
    @Size(max = 500, message = "Video URL must not exceed 500 characters")
    private String videoUrl;

    @Schema(description = "Project gallery images (screenshots, demos, etc.)", example = "[\"https://example.com/img1.jpg\", \"https://example.com/img2.jpg\"]")
    private List<String> gallery;

    @Schema(description = "Technologies used in the project", example = "[\"Java\", \"Spring Boot\", \"PostgreSQL\"]")
    private List<String> technologies;

    @Schema(description = "Project category", example = "Backend", allowableValues = {"Web", "Mobile", "Backend", "Fullstack"})
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @Schema(description = "Mark project as featured", example = "true")
    private Boolean isFeatured;

    @Schema(description = "Display order (lower number appears first)", example = "1")
    private Integer displayOrder;

    @Schema(description = "Project status", example = "completed", allowableValues = {"completed", "in_progress", "archived"})
    @Size(max = 50, message = "Status must not exceed 50 characters")
    private String status;

    @Schema(description = "Project completion/start date", example = "2025-01-01")
    private LocalDate projectDate;
}

