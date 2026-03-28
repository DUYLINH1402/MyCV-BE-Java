package com.linhnguyen.portfolio_api.auth.repository;

import com.linhnguyen.portfolio_api.auth.entity.AdminCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository cho AdminCredential entity.
 *
 * Quản lý các thao tác CRUD và truy vấn liên quan đến thông tin đăng nhập Admin.
 * Portfolio chỉ có 1 admin duy nhất nên các method đơn giản.
 */
@Repository
public interface AdminCredentialRepository extends JpaRepository<AdminCredential, Long> {

    Optional<AdminCredential> findByEmailAndIsActiveTrue(String email);

    Optional<AdminCredential> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<AdminCredential> findFirstByIsActiveTrue();
}

