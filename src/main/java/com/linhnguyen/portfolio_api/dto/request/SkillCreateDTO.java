package com.linhnguyen.portfolio_api.dto.request;

import com.linhnguyen.portfolio_api.entity.SkillCategory;
import com.linhnguyen.portfolio_api.entity.SkillLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO chứa dữ liệu để tạo mới Skill.
 * Sử dụng Jakarta Validation để validate input từ client.
 */
@Getter
@Setter
@Builder
@Schema(description = "New skill creation payload")
public class SkillCreateDTO {

    @Schema(description = "Skill name", example = "Java", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Skill name is required")
    @Size(max = 100, message = "Skill name must not exceed 100 characters")
    private String name;

    @Schema(description = "Skill category classification", example = "BACKEND", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Category is required")
    private SkillCategory category;

    @Schema(description = "Proficiency level", example = "HIGH", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Proficiency level is required")
    private SkillLevel level;

    @Schema(description = "Display priority order - lower value appears first", example = "1")
    @Min(value = 0, message = "Priority must be a non-negative number")
    private Integer priority;
}
