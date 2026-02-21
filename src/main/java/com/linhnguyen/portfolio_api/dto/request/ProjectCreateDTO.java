package com.linhnguyen.portfolio_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO chứa dữ liệu để tạo mới Project.
 * Sử dụng Jakarta Validation để validate input từ client.
 */
@Getter
@Setter
@Builder
@Schema(description = "Thông tin để tạo mới Project")
public class ProjectCreateDTO {

    @Schema(description = "Tiêu đề của dự án", example = "Portfolio API", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Tiêu đề là bắt buộc")
    @Size(max = 255, message = "Tiêu đề không được vượt quá 255 ký tự")
    private String title;

    @Schema(description = "Mô tả ngắn gọn về dự án (hiển thị ở danh sách)", example = "RESTful API cho Portfolio cá nhân")
    @Size(max = 500, message = "Mô tả ngắn không được vượt quá 500 ký tự")
    private String shortDescription;

    @Schema(description = "Mô tả chi tiết đầy đủ về dự án (dạng JSON). Có thể chứa cấu trúc phức tạp như: sections, bullet points, highlights...")
    private Map<String, Object> fullDescription;

    @Schema(description = "Đường dẫn đến hình ảnh thumbnail", example = "https://example.com/project-image.jpg")
    @Size(max = 255, message = "Image URL không được vượt quá 255 ký tự")
    private String imageUrl;

    @Schema(description = "Đường dẫn đến trang demo trực tiếp", example = "https://demo.example.com")
    @Size(max = 255, message = "Demo URL không được vượt quá 255 ký tự")
    private String demoUrl;

    @Schema(description = "Đường dẫn đến GitHub repository", example = "https://github.com/linhnguyen/portfolio-api")
    @Size(max = 255, message = "GitHub URL không được vượt quá 255 ký tự")
    private String githubUrl;

    @Schema(description = "Đường dẫn đến bài review/blog về dự án", example = "https://blog.example.com/portfolio-api")
    @Size(max = 500, message = "Review URL không được vượt quá 500 ký tự")
    private String reviewUrl;

    @Schema(description = "Đường dẫn đến video demo/giới thiệu dự án (YouTube, Vimeo, v.v.)", example = "https://youtube.com/watch?v=abc123")
    @Size(max = 500, message = "Video URL không được vượt quá 500 ký tự")
    private String videoUrl;

    @Schema(description = "Danh sách hình ảnh gallery của dự án (screenshot, demo, v.v.)", example = "[\"https://example.com/img1.jpg\", \"https://example.com/img2.jpg\"]")
    private List<String> gallery;

    @Schema(description = "Danh sách công nghệ sử dụng trong dự án", example = "[\"Java\", \"Spring Boot\", \"PostgreSQL\"]")
    private List<String> technologies;

    @Schema(description = "Phân loại dự án", example = "Backend", allowableValues = {"Web", "Mobile", "Backend", "Fullstack"})
    @Size(max = 100, message = "Category không được vượt quá 100 ký tự")
    private String category;

    @Schema(description = "Đánh dấu dự án nổi bật", example = "true")
    private Boolean isFeatured;

    @Schema(description = "Thứ tự hiển thị (số nhỏ hiển thị trước)", example = "1")
    private Integer displayOrder;

    @Schema(description = "Trạng thái dự án", example = "completed", allowableValues = {"completed", "in_progress", "archived"})
    @Size(max = 50, message = "Status không được vượt quá 50 ký tự")
    private String status;

    @Schema(description = "Ngày hoàn thành/bắt đầu dự án", example = "2025-01-01")
    private LocalDate projectDate;
}
