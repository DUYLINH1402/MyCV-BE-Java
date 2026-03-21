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
@Schema(description = "Detailed skill information")
public class SkillResponseDTO {

    @Schema(description = "Skill ID", example = "1")
    private Long id;

    @Schema(description = "Skill name", example = "Java")
    private String name;

    @Schema(description = "Skill category classification", example = "BACKEND")
    private SkillCategory category;

    @Schema(description = "Proficiency level", example = "HIGH")
    private SkillLevel level;

    @Schema(description = "Display priority order (lower value appears first)", example = "1")
    private Integer priority;

    @Schema(description = "Record creation timestamp", example = "2025-01-11T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last updated timestamp", example = "2025-01-11T15:45:00")
    private LocalDateTime updatedAt;
}
