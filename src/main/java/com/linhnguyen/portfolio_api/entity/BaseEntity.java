package com.linhnguyen.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Lớp Entity cơ sở chứa các trường audit chung.
 * Tất cả các entity khác nên kế thừa lớp này để đảm bảo tính nhất quán trong việc theo dõi lịch sử thay đổi.
 *
 * Các trường audit bao gồm:
 * - createdAt: Thời điểm tạo bản ghi
 * - updatedAt: Thời điểm cập nhật gần nhất
 * - createdBy: Người tạo bản ghi
 * - updatedBy: Người cập nhật gần nhất
 * - isDeleted: Cờ đánh dấu xóa mềm (soft delete)
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
