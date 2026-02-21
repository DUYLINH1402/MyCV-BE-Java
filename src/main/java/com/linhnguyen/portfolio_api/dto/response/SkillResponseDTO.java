package com.linhnguyen.portfolio_api.dto.response;

import com.linhnguyen.portfolio_api.entity.SkillCategory;
import com.linhnguyen.portfolio_api.entity.SkillLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * DTO chứa dữ liệu Skill trả về cho client.
 * Chỉ bao gồm các thông tin cần thiết cho việc hiển thị kỹ năng.
 */
@Getter
@Builder
@Schema(description = "Thông tin chi tiết của kỹ năng")
public class SkillResponseDTO {

    @Schema(description = "ID của kỹ năng", example = "1")
    private Long id;

    @Schema(description = "Tên của kỹ năng", example = "Java")
    private String name;

    @Schema(description = "Danh mục phân loại kỹ năng", example = "BACKEND")
    private SkillCategory category;

    @Schema(description = "Mức độ thành thạo", example = "HIGH")
    private SkillLevel level;

    @Schema(description = "Thứ tự ưu tiên hiển thị (số nhỏ hiển thị trước)", example = "1")
    private Integer priority;

    @Schema(description = "Thời điểm tạo bản ghi", example = "2025-01-11T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Thời điểm cập nhật gần nhất", example = "2025-01-11T15:45:00")
    private LocalDateTime updatedAt;
}
