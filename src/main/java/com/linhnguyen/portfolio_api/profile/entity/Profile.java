package com.linhnguyen.portfolio_api.profile.entity;

import com.linhnguyen.portfolio_api.entity.BaseEntity;
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

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "title")
    private String title;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "professional_summary", length = 500)
    private String professionalSummary;

    @Column(name = "experience_years", length = 50)
    private String experienceYears;

    @Column(name = "total_projects", length = 50)
    private String totalProjects;

    @Column(name = "education_summary", length = 255)
    private String educationSummary;

    @Column(name = "cert_summary", length = 255)
    private String certSummary;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "cv_url")
    private String cvUrl;
}

