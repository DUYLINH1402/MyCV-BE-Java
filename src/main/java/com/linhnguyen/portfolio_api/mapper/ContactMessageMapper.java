package com.linhnguyen.portfolio_api.mapper;

import com.linhnguyen.portfolio_api.dto.request.ContactRequestDTO;
import com.linhnguyen.portfolio_api.dto.response.ContactResponseDTO;
import com.linhnguyen.portfolio_api.entity.ContactMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper chuyển đổi giữa ContactMessage Entity và DTO.
 * Sử dụng MapStruct để tự động generate implementation.
 */
@Mapper(componentModel = "spring")
public interface ContactMessageMapper {

    /**
     * Chuyển đổi từ Request DTO sang Entity.
     * Map field name -> senderName, email -> senderEmail
     *
     * @param dto Request DTO
     * @return ContactMessage entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senderName", source = "name")
    @Mapping(target = "senderEmail", source = "email")
    @Mapping(target = "isRead", ignore = true)
    @Mapping(target = "emailSent", ignore = true)
    @Mapping(target = "senderIp", ignore = true)
    ContactMessage toEntity(ContactRequestDTO dto);

    /**
     * Chuyển đổi từ Entity sang Response DTO.
     *
     * @param entity ContactMessage entity
     * @return Response DTO
     */
    @Mapping(target = "confirmationMessage", ignore = true)
    ContactResponseDTO toResponseDTO(ContactMessage entity);
}

