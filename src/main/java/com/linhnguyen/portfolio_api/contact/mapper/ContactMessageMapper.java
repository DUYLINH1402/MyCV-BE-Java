package com.linhnguyen.portfolio_api.contact.mapper;

import com.linhnguyen.portfolio_api.contact.dto.ContactRequestDTO;
import com.linhnguyen.portfolio_api.contact.dto.ContactResponseDTO;
import com.linhnguyen.portfolio_api.contact.entity.ContactMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper chuyển đổi giữa ContactMessage Entity và DTO.
 */
@Mapper(componentModel = "spring")
public interface ContactMessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senderName", source = "name")
    @Mapping(target = "senderEmail", source = "email")
    @Mapping(target = "isRead", ignore = true)
    @Mapping(target = "emailSent", ignore = true)
    @Mapping(target = "senderIp", ignore = true)
    ContactMessage toEntity(ContactRequestDTO dto);

    @Mapping(target = "confirmationMessage", ignore = true)
    ContactResponseDTO toResponseDTO(ContactMessage entity);
}

