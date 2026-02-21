package com.linhnguyen.portfolio_api.repository;

import com.linhnguyen.portfolio_api.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository xử lý các thao tác với bảng profile trong database.
 * Kế thừa JpaRepository để có sẵn các method CRUD cơ bản.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /**
     * Tìm profile theo địa chỉ email (chỉ lấy profile chưa bị xóa).
     *
     * @param email Địa chỉ email cần tìm
     * @return Optional chứa Profile nếu tìm thấy
     */
    Optional<Profile> findByEmailAndIsDeletedFalse(String email);

    /**
     * Lấy danh sách tất cả profile đang hoạt động (chưa bị xóa mềm).
     *
     * @return Danh sách Profile
     */
    List<Profile> findAllByIsDeletedFalse();

    /**
     * Tìm profile theo ID (chỉ lấy profile chưa bị xóa).
     *
     * @param id ID của profile cần tìm
     * @return Optional chứa Profile nếu tìm thấy
     */
    Optional<Profile> findByIdAndIsDeletedFalse(Long id);

    /**
     * Lấy profile đầu tiên chưa bị xóa.
     * Dùng cho Portfolio vì chỉ có 1 owner profile duy nhất.
     *
     * @return Optional chứa Profile nếu có
     */
    Optional<Profile> findFirstByIsDeletedFalse();
}

