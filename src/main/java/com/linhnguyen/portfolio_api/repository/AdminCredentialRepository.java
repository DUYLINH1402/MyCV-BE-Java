package com.linhnguyen.portfolio_api.repository;

import com.linhnguyen.portfolio_api.entity.AdminCredential;
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

    /**
     * Tìm kiếm Admin credential theo email và trạng thái active.
     *
     * @param email Email của admin
     * @return Optional chứa AdminCredential nếu tìm thấy
     */
    Optional<AdminCredential> findByEmailAndIsActiveTrue(String email);

    /**
     * Tìm kiếm Admin credential theo email (không kiểm tra trạng thái).
     *
     * @param email Email của admin
     * @return Optional chứa AdminCredential nếu tìm thấy
     */
    Optional<AdminCredential> findByEmail(String email);

    /**
     * Kiểm tra email đã tồn tại chưa.
     *
     * @param email Email cần kiểm tra
     * @return true nếu email đã tồn tại
     */
    boolean existsByEmail(String email);

    /**
     * Lấy credential đầu tiên đang active.
     * Dùng cho Portfolio cá nhân (chỉ có 1 owner).
     *
     * @return Optional chứa AdminCredential nếu tìm thấy
     */
    Optional<AdminCredential> findFirstByIsActiveTrue();
}

