package com.linhnguyen.portfolio_api.experience.mapper;

import com.linhnguyen.portfolio_api.experience.dto.ExperienceCreateDTO;
import com.linhnguyen.portfolio_api.experience.dto.ExperienceUpdateDTO;
import com.linhnguyen.portfolio_api.experience.dto.ExperienceResponseDTO;
import com.linhnguyen.portfolio_api.experience.entity.ProfessionalExperience;
import org.mapstruct.*;

/**
 * Mapper chuyển đổi giữa ProfessionalExperience Entity và các DTO.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExperienceMapper {

    ExperienceResponseDTO toResponseDTO(ProfessionalExperience experience);

    @Mapping(target = "id", ignore = true)
    ProfessionalExperience toEntity(ExperienceCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ExperienceUpdateDTO dto, @MappingTarget ProfessionalExperience experience);
}

