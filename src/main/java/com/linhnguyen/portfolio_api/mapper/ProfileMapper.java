package com.linhnguyen.portfolio_api.mapper;

import com.linhnguyen.portfolio_api.dto.request.ProfileUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.ProfileResponseDTO;
import com.linhnguyen.portfolio_api.entity.Profile;
import org.mapstruct.*;

/**
 * Mapper chuyển đổi giữa Profile Entity và các DTO.
 * MapStruct sẽ tự động generate implementation tại compile time.
 * Sử dụng componentModel = "spring" để inject như một Spring Bean.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileMapper {

    /**
     * Chuyển đổi Profile Entity sang ProfileResponseDTO.
     * Dùng khi trả về dữ liệu profile cho client.
     *
     * @param profile Entity cần chuyển đổi
     * @return DTO chứa thông tin profile
     */
    ProfileResponseDTO toResponseDTO(Profile profile);


    /**
     * Cập nhật Profile Entity từ ProfileUpdateDTO.
     * Chỉ cập nhật các trường có giá trị (non-null) trong DTO.
     * Sử dụng @MappingTarget để cập nhật trực tiếp vào entity hiện có.
     *
     * @param dto     DTO chứa dữ liệu cập nhật
     * @param profile Entity cần cập nhật
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProfileUpdateDTO dto, @MappingTarget Profile profile);
}

