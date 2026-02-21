package com.linhnguyen.portfolio_api.dto.request;

import com.linhnguyen.portfolio_api.entity.SkillCategory;
import com.linhnguyen.portfolio_api.entity.SkillLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO chứa dữ liệu để cập nhật Skill.
 * Tất cả các trường đều optional - chỉ cập nhật những trường được gửi lên.
 */
@Getter
@Setter
@Builder
@Schema(description = "Thông tin cập nhật Skill (tất cả trường đều optional)")
public class SkillUpdateDTO {

    @Schema(description = "Tên của kỹ năng", example = "Java")
    @Size(max = 100, message = "Tên kỹ năng không được vượt quá 100 ký tự")
    private String name;

    @Schema(description = "Danh mục phân loại kỹ năng", example = "BACKEND")
    private SkillCategory category;

    @Schema(description = "Mức độ thành thạo", example = "HIGH")
    private SkillLevel level;

    @Schema(description = "Thứ tự ưu tiên hiển thị - giá trị nhỏ sẽ hiển thị trước", example = "1")
    @Min(value = 0, message = "Priority phải là số không âm")
    private Integer priority;
}
