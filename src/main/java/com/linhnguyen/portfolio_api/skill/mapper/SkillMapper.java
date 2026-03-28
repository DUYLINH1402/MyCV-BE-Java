package com.linhnguyen.portfolio_api.skill.mapper;

import com.linhnguyen.portfolio_api.skill.dto.SkillCreateDTO;
import com.linhnguyen.portfolio_api.skill.dto.SkillUpdateDTO;
import com.linhnguyen.portfolio_api.skill.dto.SkillResponseDTO;
import com.linhnguyen.portfolio_api.skill.entity.Skill;
import org.mapstruct.*;

/**
 * Mapper chuyển đổi giữa Skill Entity và các DTO.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SkillMapper {

    SkillResponseDTO toResponseDTO(Skill skill);

    Skill toEntity(SkillCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(SkillUpdateDTO dto, @MappingTarget Skill skill);
}

