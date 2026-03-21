package com.linhnguyen.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity đại diện cho kinh nghiệm làm việc chuyên nghiệp trong Portfolio.
 * Lưu trữ thông tin về chức danh, công ty, thời gian và mô tả công việc.
 * Trường description lưu dạng JSONB (List các bullet points).
 */
@Entity
@Table(name = "professional_experiences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalExperience extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Chức danh/vị trí công việc - bắt buộc */
    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    /** Tên công ty/tổ chức - bắt buộc */
    @Column(name = "company", nullable = false)
    private String company;

    /** URL logo công ty (hiển thị trên timeline) */
    @Column(name = "company_logo", length = 500)
    private String companyLogo;

    /** Địa điểm làm việc (thành phố, quốc gia) */
    @Column(name = "location")
    private String location;

    /** Ngày bắt đầu làm việc - bắt buộc */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Ngày kết thúc (NULL nếu đang làm việc hiện tại) */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Mô tả chi tiết công việc dạng JSONB.
     * Chứa danh sách các bullet points mô tả trách nhiệm, thành tựu.
     * Ví dụ: ["Phát triển REST API với Spring Boot", "Tối ưu hiệu năng database"]
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description", columnDefinition = "jsonb")
    @Builder.Default
    private List<String> description = List.of();
}

