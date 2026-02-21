package com.linhnguyen.portfolio_api.repository;

import com.linhnguyen.portfolio_api.entity.Skill;
import com.linhnguyen.portfolio_api.entity.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository xử lý các thao tác với bảng skills trong database.
 * Kế thừa JpaRepository để có sẵn các method CRUD cơ bản.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    /**
     * Lấy danh sách tất cả kỹ năng đang hoạt động (chưa bị xóa mềm).
     *
     * @return Danh sách Skill
     */
    List<Skill> findAllByIsDeletedFalse();

    /**
     * Tìm kỹ năng theo ID (chỉ lấy kỹ năng chưa bị xóa).
     *
     * @param id ID của kỹ năng cần tìm
     * @return Optional chứa Skill nếu tìm thấy
     */
    Optional<Skill> findByIdAndIsDeletedFalse(Long id);

    /**
     * Lấy danh sách kỹ năng theo danh mục phân loại.
     * Ví dụ: Lấy tất cả kỹ năng Backend hoặc Frontend.
     *
     * @param category Danh mục cần lọc
     * @return Danh sách Skill thuộc danh mục đó
     */
    List<Skill> findByCategoryAndIsDeletedFalse(SkillCategory category);

    /**
     * Lấy danh sách kỹ năng theo danh mục, sắp xếp theo priority tăng dần.
     *
     * @param category Danh mục cần lọc
     * @return Danh sách Skill thuộc danh mục đó, đã sắp xếp theo priority
     */
    List<Skill> findByCategoryAndIsDeletedFalseOrderByPriorityAsc(SkillCategory category);

    /**
     * Kiểm tra xem kỹ năng với tên đã tồn tại hay chưa.
     * Dùng để validate khi tạo mới kỹ năng, tránh trùng lặp.
     *
     * @param name Tên kỹ năng cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByNameAndIsDeletedFalse(String name);
}
