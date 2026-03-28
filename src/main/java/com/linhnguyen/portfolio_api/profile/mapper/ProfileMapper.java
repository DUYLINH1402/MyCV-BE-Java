package com.linhnguyen.portfolio_api.profile.mapper;

import com.linhnguyen.portfolio_api.profile.dto.ProfileUpdateDTO;
import com.linhnguyen.portfolio_api.profile.dto.ProfileResponseDTO;
import com.linhnguyen.portfolio_api.profile.entity.Profile;
import org.mapstruct.*;

/**
 * Mapper chuyển đổi giữa Profile Entity và các DTO.
 * MapStruct sẽ tự động generate implementation tại compile time.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileMapper {

    ProfileResponseDTO toResponseDTO(Profile profile);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProfileUpdateDTO dto, @MappingTarget Profile profile);
}

