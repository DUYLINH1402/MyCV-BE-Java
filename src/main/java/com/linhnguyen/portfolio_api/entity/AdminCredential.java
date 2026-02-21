package com.linhnguyen.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity quản lý thông tin đăng nhập Admin.
 *
 * Tách riêng khỏi Profile để đảm bảo tính bảo mật và separation of concerns.
 * Portfolio chỉ có DUY NHẤT 1 admin (owner của portfolio).
 *
 * Lý do thiết kế:
 * - Separation of Concerns: Tách biệt thông tin hiển thị (Profile) và thông tin bảo mật (Authentication)
 * - Security: Dễ quản lý và bảo mật hơn khi tách riêng
 * - Scalability: Dễ mở rộng thêm các tính năng auth khác (OAuth, 2FA...) sau này
 */
@Entity
@Table(name = "admin_credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminCredential extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Email đăng nhập - phải là duy nhất */
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    /** Mật khẩu đăng nhập (BCrypt encoded) - không được trả về qua API */
    @Column(name = "password", nullable = false)
    private String password;

    /** Role của admin (mặc định là ADMIN) */
    @Column(name = "role", length = 20, nullable = false)
    @Builder.Default
    private String role = "ADMIN";

    /** Trạng thái tài khoản (active/inactive) */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}

