package com.linhnguyen.portfolio_api.repository;

import com.linhnguyen.portfolio_api.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository quản lý CRUD cho ContactMessage Entity.
 */
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    /**
     * Tìm tất cả tin nhắn chưa bị xóa, sắp xếp theo thời gian tạo giảm dần.
     *
     * @param pageable Thông tin phân trang
     * @return Page chứa danh sách tin nhắn
     */
    Page<ContactMessage> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Tìm tin nhắn theo ID và chưa bị xóa.
     *
     * @param id ID của tin nhắn
     * @return Optional chứa tin nhắn nếu tìm thấy
     */
    Optional<ContactMessage> findByIdAndIsDeletedFalse(Long id);

    /**
     * Đếm số tin nhắn chưa đọc.
     *
     * @return Số lượng tin nhắn chưa đọc
     */
    @Query("SELECT COUNT(c) FROM ContactMessage c WHERE c.isRead = false AND c.isDeleted = false")
    long countUnreadMessages();

    /**
     * Tìm tất cả tin nhắn chưa đọc.
     *
     * @return Danh sách tin nhắn chưa đọc
     */
    List<ContactMessage> findByIsReadFalseAndIsDeletedFalseOrderByCreatedAtDesc();

    /**
     * Kiểm tra có tin nhắn từ email trong khoảng thời gian gần đây không (chống spam).
     * Sử dụng JPQL để kiểm tra tin nhắn được tạo sau thời điểm cutoff.
     *
     * @param email Email cần kiểm tra
     * @param cutoffTime Thời điểm giới hạn (tin nhắn tạo sau thời điểm này được tính)
     * @return true nếu có tin nhắn gần đây từ email này
     */
    @Query("SELECT COUNT(c) > 0 FROM ContactMessage c WHERE c.senderEmail = :email " +
           "AND c.createdAt > :cutoffTime " +
           "AND c.isDeleted = false")
    boolean existsRecentMessageByEmail(String email, java.time.LocalDateTime cutoffTime);
}

