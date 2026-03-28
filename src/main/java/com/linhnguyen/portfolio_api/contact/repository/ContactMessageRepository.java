package com.linhnguyen.portfolio_api.contact.repository;

import com.linhnguyen.portfolio_api.contact.entity.ContactMessage;
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

    Page<ContactMessage> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    Optional<ContactMessage> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT COUNT(c) FROM ContactMessage c WHERE c.isRead = false AND c.isDeleted = false")
    long countUnreadMessages();

    List<ContactMessage> findByIsReadFalseAndIsDeletedFalseOrderByCreatedAtDesc();

    @Query("SELECT COUNT(c) > 0 FROM ContactMessage c WHERE c.senderEmail = :email " +
           "AND c.createdAt > :cutoffTime " +
           "AND c.isDeleted = false")
    boolean existsRecentMessageByEmail(String email, java.time.LocalDateTime cutoffTime);
}

