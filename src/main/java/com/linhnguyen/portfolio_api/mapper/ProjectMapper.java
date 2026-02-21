package com.linhnguyen.portfolio_api.mapper;

import com.linhnguyen.portfolio_api.dto.request.ProjectCreateDTO;
import com.linhnguyen.portfolio_api.dto.request.ProjectUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.ProjectResponseDTO;
import com.linhnguyen.portfolio_api.entity.Project;
import org.mapstruct.*;

/**
 * Mapper chuyển đổi giữa Project Entity và các DTO.
 * MapStruct sẽ tự động generate implementation tại compile time.
 * Sử dụng componentModel = "spring" để inject như một Spring Bean.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {

    /**
     * Chuyển đổi Project Entity sang ProjectResponseDTO.
     * Dùng khi trả về dữ liệu project cho client.
     *
     * @param project Entity cần chuyển đổi
     * @return DTO chứa thông tin project
     */
    ProjectResponseDTO toResponseDTO(Project project);

    /**
     * Chuyển đổi ProjectCreateDTO sang Project Entity.
     * Dùng khi tạo mới project từ dữ liệu client gửi lên.
     * Trường id được ignore vì sẽ được database tự động generate.
     * Các trường audit (createdAt, updatedAt, ...) sẽ được JPA Auditing tự động xử lý.
     *
     * @param dto DTO chứa dữ liệu tạo mới
     * @return Entity để lưu vào database
     */
    @Mapping(target = "id", ignore = true)
    Project toEntity(ProjectCreateDTO dto);

    /**
     * Cập nhật Project Entity từ ProjectUpdateDTO.
     * Chỉ cập nhật các trường có giá trị (non-null) trong DTO.
     * Sử dụng @MappingTarget để cập nhật trực tiếp vào entity hiện có.
     * Trường id được ignore vì không được phép thay đổi.
     *
     * @param dto     DTO chứa dữ liệu cập nhật
     * @param project Entity cần cập nhật
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ProjectUpdateDTO dto, @MappingTarget Project project);
}
