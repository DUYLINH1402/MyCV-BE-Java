package com.linhnguyen.portfolio_api.repository;

import com.linhnguyen.portfolio_api.entity.ProfessionalExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository xử lý các thao tác với bảng professional_experiences trong database.
 * Kế thừa JpaRepository để có sẵn các method CRUD cơ bản.
 */
@Repository
public interface ProfessionalExperienceRepository extends JpaRepository<ProfessionalExperience, Long> {

    /**
     * Lấy danh sách tất cả kinh nghiệm đang hoạt động, sắp xếp theo startDate giảm dần.
     * Kinh nghiệm gần nhất sẽ hiển thị trước.
     *
     * @return Danh sách ProfessionalExperience
     */
    List<ProfessionalExperience> findAllByIsDeletedFalseOrderByStartDateDesc();

    /**
     * Tìm kinh nghiệm theo ID (chỉ lấy record chưa bị xóa).
     *
     * @param id ID của kinh nghiệm cần tìm
     * @return Optional chứa ProfessionalExperience nếu tìm thấy
     */
    Optional<ProfessionalExperience> findByIdAndIsDeletedFalse(Long id);

    /**
     * Kiểm tra xem kinh nghiệm với cặp (jobTitle, company) đã tồn tại hay chưa.
     * Dùng để validate khi tạo mới, tránh trùng lặp.
     *
     * @param jobTitle Chức danh cần kiểm tra
     * @param company  Công ty cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByJobTitleAndCompanyAndIsDeletedFalse(String jobTitle, String company);

    /**
     * Kiểm tra xem kinh nghiệm với cặp (jobTitle, company) đã tồn tại hay chưa, loại trừ ID hiện tại.
     * Dùng để validate khi cập nhật kinh nghiệm.
     *
     * @param jobTitle Chức danh cần kiểm tra
     * @param company  Công ty cần kiểm tra
     * @param id       ID cần loại trừ
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByJobTitleAndCompanyAndIdNotAndIsDeletedFalse(String jobTitle, String company, Long id);
}

