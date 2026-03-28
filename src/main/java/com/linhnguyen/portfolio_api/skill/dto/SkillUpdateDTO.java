package com.linhnguyen.portfolio_api.skill.dto;

import com.linhnguyen.portfolio_api.skill.entity.SkillCategory;
import com.linhnguyen.portfolio_api.skill.entity.SkillLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO chứa dữ liệu để cập nhật Skill.
 * Tất cả các trường đều optional.
 */
@Getter
@Setter
@Builder
@Schema(description = "Skill update payload (all fields are optional)")
public class SkillUpdateDTO {

    @Schema(description = "Skill name", example = "Java")
    @Size(max = 100, message = "Skill name must not exceed 100 characters")
    private String name;

    @Schema(description = "Skill category classification", example = "BACKEND")
    private SkillCategory category;

    @Schema(description = "Proficiency level", example = "HIGH")
    private SkillLevel level;

    @Schema(description = "Display priority order - lower value appears first", example = "1")
    @Min(value = 0, message = "Priority must be a non-negative number")
    private Integer priority;
}

