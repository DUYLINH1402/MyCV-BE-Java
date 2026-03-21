package com.linhnguyen.portfolio_api.mapper;

import com.linhnguyen.portfolio_api.dto.request.ExperienceCreateDTO;
import com.linhnguyen.portfolio_api.dto.request.ExperienceUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.ExperienceResponseDTO;
import com.linhnguyen.portfolio_api.entity.ProfessionalExperience;
import org.mapstruct.*;

/**
 * Mapper chuyển đổi giữa ProfessionalExperience Entity và các DTO.
 * MapStruct sẽ tự động generate implementation tại compile time.
 * Sử dụng componentModel = "spring" để inject như một Spring Bean.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExperienceMapper {

    /**
     * Chuyển đổi ProfessionalExperience Entity sang ExperienceResponseDTO.
     * Dùng khi trả về dữ liệu experience cho client.
     *
     * @param experience Entity cần chuyển đổi
     * @return DTO chứa thông tin experience
     */
    ExperienceResponseDTO toResponseDTO(ProfessionalExperience experience);

    /**
     * Chuyển đổi ExperienceCreateDTO sang ProfessionalExperience Entity.
     * Dùng khi tạo mới experience từ dữ liệu client gửi lên.
     * Trường id được ignore vì sẽ được database tự động generate.
     *
     * @param dto DTO chứa dữ liệu tạo mới
     * @return Entity để lưu vào database
     */
    @Mapping(target = "id", ignore = true)
    ProfessionalExperience toEntity(ExperienceCreateDTO dto);

    /**
     * Cập nhật ProfessionalExperience Entity từ ExperienceUpdateDTO.
     * Chỉ cập nhật các trường có giá trị (non-null) trong DTO.
     * Sử dụng @MappingTarget để cập nhật trực tiếp vào entity hiện có.
     *
     * @param dto        DTO chứa dữ liệu cập nhật
     * @param experience Entity cần cập nhật
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ExperienceUpdateDTO dto, @MappingTarget ProfessionalExperience experience);
}

