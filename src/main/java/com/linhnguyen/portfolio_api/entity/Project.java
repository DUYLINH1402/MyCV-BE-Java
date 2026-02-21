package com.linhnguyen.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Entity đại diện cho các dự án trong Portfolio.
 * Lưu trữ thông tin về tiêu đề, mô tả, hình ảnh và các đường dẫn liên quan đến dự án.
 * Hỗ trợ lưu trữ dữ liệu JSONB cho fullDescription và technologies.
 */
@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tiêu đề của dự án - bắt buộc */
    @Column(name = "title", nullable = false)
    private String title;

    /** Mô tả ngắn gọn về dự án (hiển thị ở danh sách) */
    @Column(name = "short_description", length = 500)
    private String shortDescription;

    /**
     * Mô tả chi tiết đầy đủ về dự án (dạng JSONB).
     * Có thể chứa cấu trúc phức tạp như: sections, bullet points, highlights...
     * Ví dụ: {"overview": "...", "features": [...], "challenges": "..."}
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "full_description", columnDefinition = "jsonb")
    private Map<String, Object> fullDescription;

    /** Đường dẫn đến hình ảnh thumbnail của dự án */
    @Column(name = "image_url")
    private String imageUrl;

    /** Đường dẫn đến trang demo trực tiếp */
    @Column(name = "demo_url")
    private String demoUrl;

    /** Đường dẫn đến GitHub repository source code */
    @Column(name = "github_url")
    private String githubUrl;

    /** Đường dẫn đến bài review/blog về dự án */
    @Column(name = "review_url", length = 500)
    private String reviewUrl;

    /** Đường dẫn đến video demo/giới thiệu dự án (YouTube, Vimeo, v.v.) */
    @Column(name = "video_url", length = 500)
    private String videoUrl;

    /**
     * Danh sách hình ảnh gallery của dự án (dạng JSONB).
     * Chứa các URL hình ảnh chi tiết, screenshot, v.v.
     * Ví dụ: ["https://example.com/img1.jpg", "https://example.com/img2.jpg"]
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "gallery", columnDefinition = "jsonb")
    @Builder.Default
    private List<String> gallery = List.of();

    /**
     * Danh sách công nghệ sử dụng trong dự án (dạng JSONB).
     * Ví dụ: ["Java", "Spring Boot", "PostgreSQL", "Docker"]
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "technologies", columnDefinition = "jsonb")
    private List<String> technologies;

    /** Phân loại dự án (ví dụ: "Web", "Mobile", "Backend", "Fullstack") */
    @Column(name = "category", length = 100)
    private String category;

    /** Đánh dấu dự án nổi bật để hiển thị ưu tiên */
    @Column(name = "is_featured")
    @Builder.Default
    private Boolean isFeatured = false;

    /** Thứ tự hiển thị (số nhỏ hơn hiển thị trước) */
    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    /** Trạng thái dự án (completed, in_progress, archived) */
    @Column(name = "status", length = 50)
    @Builder.Default
    private String status = "completed";

    /** Ngày bắt đầu hoặc hoàn thành dự án */
    @Column(name = "project_date")
    private LocalDate projectDate;
}
