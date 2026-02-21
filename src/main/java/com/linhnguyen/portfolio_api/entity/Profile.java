package com.linhnguyen.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity đại diện cho thông tin cá nhân trong Portfolio.
 * Lưu trữ các thông tin như họ tên, chức danh, tiểu sử, email và các liên kết mạng xã hội.
 */
@Entity
@Table(name = "profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Họ và tên đầy đủ */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /** Chức danh / Vị trí công việc */
    @Column(name = "title")
    private String title;

    /** Tiểu sử / Giới thiệu bản thân */
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    /** Tóm tắt chuyên môn - Đoạn text ngắn hiển thị ở trang chủ */
    @Column(name = "professional_summary", length = 500)
    private String professionalSummary;

    /** Số năm kinh nghiệm - Ví dụ: "3+ Years Experience" */
    @Column(name = "experience_years", length = 50)
    private String experienceYears;

    /** Tổng số dự án đã thực hiện - Ví dụ: "15+ Projects" */
    @Column(name = "total_projects", length = 50)
    private String totalProjects;

    /** Tóm tắt học vấn - Ví dụ: "Computer Science" */
    @Column(name = "education_summary", length = 255)
    private String educationSummary;

    /** Tóm tắt chứng chỉ - Ví dụ: "AWS, Spring Professional" */
    @Column(name = "cert_summary", length = 255)
    private String certSummary;

    /** Địa chỉ email liên hệ */
    @Column(name = "email", length = 100)
    private String email;

    /** Số điện thoại liên hệ */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /** Đường dẫn đến trang GitHub */
    @Column(name = "github_url")
    private String githubUrl;

    /** Đường dẫn đến trang LinkedIn */
    @Column(name = "linkedin_url")
    private String linkedinUrl;

    /** Đường dẫn đến ảnh đại diện */
    @Column(name = "avatar_url")
    private String avatarUrl;
}
