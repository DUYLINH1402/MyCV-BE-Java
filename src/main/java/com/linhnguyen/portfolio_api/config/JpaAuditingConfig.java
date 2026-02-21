package com.linhnguyen.portfolio_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Cấu hình JPA Auditing.
 * Tự động điền các trường audit: createdAt, updatedAt, createdBy, updatedBy.
 * Khi entity được tạo hoặc cập nhật, các trường này sẽ được tự động gán giá trị.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    /**
     * Cung cấp thông tin người dùng hiện tại cho các trường audit (createdBy, updatedBy).
     *
     * TODO: Thay thế bằng user thực từ SecurityContext khi tích hợp Spring Security.
     * Hiện tại trả về "SYSTEM" làm giá trị mặc định.
     *
     * @return AuditorAware chứa thông tin auditor
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        // Tạm thời trả về SYSTEM, sẽ thay bằng authenticated user sau khi có Security
        return () -> Optional.of("SYSTEM");
    }
}
