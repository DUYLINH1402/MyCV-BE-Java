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
@Schema(description = "Thông tin chi tiết của dự án")
public class ProjectResponseDTO {

    @Schema(description = "ID của dự án", example = "1")
    private Long id;

    @Schema(description = "Tiêu đề của dự án", example = "Portfolio API")
    private String title;

    @Schema(description = "Mô tả ngắn gọn về dự án (hiển thị ở danh sách)", example = "RESTful API cho Portfolio cá nhân")
    private String shortDescription;

    @Schema(description = "Mô tả chi tiết đầy đủ về dự án (dạng JSON). Chứa cấu trúc phức tạp như: sections, bullet points, highlights...")
    private Map<String, Object> fullDescription;

    @Schema(description = "Đường dẫn đến hình ảnh thumbnail", example = "https://example.com/project-image.jpg")
    private String imageUrl;

    @Schema(description = "Đường dẫn đến trang demo trực tiếp", example = "https://demo.example.com")
    private String demoUrl;

    @Schema(description = "Đường dẫn đến GitHub repository", example = "https://github.com/linhnguyen/portfolio-api")
    private String githubUrl;

    @Schema(description = "Đường dẫn đến bài review/blog về dự án", example = "https://blog.example.com/portfolio-api")
    private String reviewUrl;

    @Schema(description = "Đường dẫn đến video demo/giới thiệu dự án (YouTube, Vimeo, v.v.)", example = "https://youtube.com/watch?v=abc123")
    private String videoUrl;

    @Schema(description = "Danh sách hình ảnh gallery của dự án (screenshot, demo, v.v.)", example = "[\"https://example.com/img1.jpg\", \"https://example.com/img2.jpg\"]")
    private List<String> gallery;

    @Schema(description = "Danh sách công nghệ sử dụng trong dự án", example = "[\"Java\", \"Spring Boot\", \"PostgreSQL\"]")
    private List<String> technologies;

    @Schema(description = "Phân loại dự án", example = "Backend", allowableValues = {"Web", "Mobile", "Backend", "Fullstack"})
    private String category;

    @Schema(description = "Đánh dấu dự án nổi bật", example = "true")
    private Boolean isFeatured;

    @Schema(description = "Thứ tự hiển thị", example = "1")
    private Integer displayOrder;

    @Schema(description = "Trạng thái dự án", example = "completed", allowableValues = {"completed", "in_progress", "archived"})
    private String status;

    @Schema(description = "Ngày hoàn thành/bắt đầu dự án", example = "2025-01-01")
    private LocalDate projectDate;

    @Schema(description = "Thời điểm tạo bản ghi", example = "2025-01-11T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Thời điểm cập nhật gần nhất", example = "2025-01-11T15:45:00")
    private LocalDateTime updatedAt;
}
