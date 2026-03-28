package com.linhnguyen.portfolio_api.project.entity;

import com.linhnguyen.portfolio_api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Entity đại diện cho các dự án trong Portfolio.
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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "full_description", columnDefinition = "jsonb")
    private Map<String, Object> fullDescription;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "demo_url")
    private String demoUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "review_url", length = 500)
    private String reviewUrl;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "gallery", columnDefinition = "jsonb")
    @Builder.Default
    private List<String> gallery = List.of();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "technologies", columnDefinition = "jsonb")
    private List<String> technologies;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "is_featured")
    @Builder.Default
    private Boolean isFeatured = false;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "status", length = 50)
    @Builder.Default
    private String status = "completed";

    @Column(name = "project_date")
    private LocalDate projectDate;
}

