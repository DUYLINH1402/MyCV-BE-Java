package com.linhnguyen.portfolio_api.project.mapper;

import com.linhnguyen.portfolio_api.project.dto.ProjectCreateDTO;
import com.linhnguyen.portfolio_api.project.dto.ProjectUpdateDTO;
import com.linhnguyen.portfolio_api.project.dto.ProjectResponseDTO;
import com.linhnguyen.portfolio_api.project.entity.Project;
import org.mapstruct.*;

/**
 * Mapper chuyển đổi giữa Project Entity và các DTO.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {

    ProjectResponseDTO toResponseDTO(Project project);

    @Mapping(target = "id", ignore = true)
    Project toEntity(ProjectCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ProjectUpdateDTO dto, @MappingTarget Project project);
}

