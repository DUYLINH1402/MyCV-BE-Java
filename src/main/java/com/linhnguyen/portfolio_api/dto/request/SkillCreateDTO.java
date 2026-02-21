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
@Schema(description = "Thông tin để tạo mới Skill")
public class SkillCreateDTO {

    @Schema(description = "Tên của kỹ năng", example = "Java", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Tên kỹ năng là bắt buộc")
    @Size(max = 100, message = "Tên kỹ năng không được vượt quá 100 ký tự")
    private String name;

    @Schema(description = "Danh mục phân loại kỹ năng", example = "BACKEND", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Danh mục là bắt buộc")
    private SkillCategory category;

    @Schema(description = "Mức độ thành thạo", example = "HIGH", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Mức độ thành thạo là bắt buộc")
    private SkillLevel level;

    @Schema(description = "Thứ tự ưu tiên hiển thị - giá trị nhỏ sẽ hiển thị trước", example = "1")
    @Min(value = 0, message = "Priority phải là số không âm")
    private Integer priority;
}
