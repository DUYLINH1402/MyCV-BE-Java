package com.linhnguyen.portfolio_api.repository;

import com.linhnguyen.portfolio_api.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository xử lý các thao tác với bảng projects trong database.
 * Kế thừa JpaRepository để có sẵn các method CRUD cơ bản.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Lấy danh sách tất cả dự án đang hoạt động, sắp xếp theo displayOrder tăng dần.
     * Dự án có displayOrder nhỏ hơn sẽ hiển thị trước.
     *
     * @return Danh sách Project
     */
    List<Project> findAllByIsDeletedFalseOrderByDisplayOrderAsc();

    /**
     * Lấy danh sách dự án nổi bật, sắp xếp theo displayOrder.
     * Chỉ lấy các dự án được đánh dấu isFeatured = true.
     *
     * @return Danh sách Project nổi bật
     */
    List<Project> findAllByIsFeaturedTrueAndIsDeletedFalseOrderByDisplayOrderAsc();

    /**
     * Lấy danh sách dự án theo category, sắp xếp theo displayOrder.
     *
     * @param category Phân loại dự án
     * @return Danh sách Project theo category
     */
    List<Project> findAllByCategoryAndIsDeletedFalseOrderByDisplayOrderAsc(String category);

    /**
     * Lấy danh sách dự án theo status, sắp xếp theo displayOrder.
     *
     * @param status Trạng thái dự án (completed, in_progress, archived)
     * @return Danh sách Project theo status
     */
    List<Project> findAllByStatusAndIsDeletedFalseOrderByDisplayOrderAsc(String status);

    /**
     * Tìm dự án theo ID (chỉ lấy dự án chưa bị xóa).
     *
     * @param id ID của dự án cần tìm
     * @return Optional chứa Project nếu tìm thấy
     */
    Optional<Project> findByIdAndIsDeletedFalse(Long id);

    /**
     * Kiểm tra xem dự án với tiêu đề đã tồn tại hay chưa.
     * Dùng để validate khi tạo mới dự án, tránh trùng lặp.
     *
     * @param title Tiêu đề cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByTitleAndIsDeletedFalse(String title);

    /**
     * Kiểm tra xem dự án với tiêu đề đã tồn tại hay chưa, loại trừ ID hiện tại.
     * Dùng để validate khi cập nhật dự án.
     *
     * @param title Tiêu đề cần kiểm tra
     * @param id    ID cần loại trừ
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByTitleAndIdNotAndIsDeletedFalse(String title, Long id);
}
