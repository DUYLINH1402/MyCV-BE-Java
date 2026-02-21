package com.linhnguyen.portfolio_api.mapper;

import com.linhnguyen.portfolio_api.dto.request.SkillCreateDTO;
import com.linhnguyen.portfolio_api.dto.request.SkillUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.SkillResponseDTO;
import com.linhnguyen.portfolio_api.entity.Skill;
import org.mapstruct.*;

/**
 * Mapper chuyển đổi giữa Skill Entity và các DTO.
 * MapStruct sẽ tự động generate implementation tại compile time.
 * Sử dụng componentModel = "spring" để inject như một Spring Bean.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SkillMapper {

    /**
     * Chuyển đổi Skill Entity sang SkillResponseDTO.
     * Dùng khi trả về dữ liệu skill cho client.
     *
     * @param skill Entity cần chuyển đổi
     * @return DTO chứa thông tin skill
     */
    SkillResponseDTO toResponseDTO(Skill skill);

    /**
     * Chuyển đổi SkillCreateDTO sang Skill Entity.
     * Dùng khi tạo mới skill từ dữ liệu client gửi lên.
     *
     * @param dto DTO chứa dữ liệu tạo mới
     * @return Entity để lưu vào database
     */
    Skill toEntity(SkillCreateDTO dto);

    /**
     * Cập nhật Skill Entity từ SkillUpdateDTO.
     * Chỉ cập nhật các trường có giá trị (non-null) trong DTO.
     * Sử dụng @MappingTarget để cập nhật trực tiếp vào entity hiện có.
     *
     * @param dto   DTO chứa dữ liệu cập nhật
     * @param skill Entity cần cập nhật
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(SkillUpdateDTO dto, @MappingTarget Skill skill);
}
