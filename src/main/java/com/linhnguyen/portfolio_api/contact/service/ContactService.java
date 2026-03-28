package com.linhnguyen.portfolio_api.contact.service;

import com.linhnguyen.portfolio_api.contact.dto.ContactRequestDTO;
import com.linhnguyen.portfolio_api.contact.dto.ContactResponseDTO;
import com.linhnguyen.portfolio_api.contact.entity.ContactMessage;
import com.linhnguyen.portfolio_api.exception.BusinessException;
import com.linhnguyen.portfolio_api.contact.mapper.ContactMessageMapper;
import com.linhnguyen.portfolio_api.contact.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service xử lý logic nghiệp vụ cho module Contact.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;
    private final BrevoEmailService brevoEmailService;

    private static final int SPAM_PROTECTION_MINUTES = 5;

    @Transactional
    public ContactResponseDTO submitContactMessage(ContactRequestDTO requestDTO, String clientIp) {
        log.info("Nhận tin nhắn liên hệ từ: {} <{}>", requestDTO.getName(), requestDTO.getEmail());

        checkSpamProtection(requestDTO.getEmail());

        ContactMessage contactMessage = contactMessageMapper.toEntity(requestDTO);
        contactMessage.setSenderIp(clientIp);

        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        log.info("Đã lưu tin nhắn liên hệ. ID: {}", savedMessage.getId());

        sendEmailNotificationAsync(savedMessage);

        ContactResponseDTO responseDTO = contactMessageMapper.toResponseDTO(savedMessage);
        responseDTO.setConfirmationMessage(
                "Thank you for reaching out! Your message has been sent successfully. " +
                "I will respond to your email as soon as possible."
        );

        return responseDTO;
    }

    private void checkSpamProtection(String email) {
        try {
            java.time.LocalDateTime cutoffTime = java.time.LocalDateTime.now()
                    .minusMinutes(SPAM_PROTECTION_MINUTES);

            boolean hasRecentMessage = contactMessageRepository.existsRecentMessageByEmail(
                    email, cutoffTime
            );

            if (hasRecentMessage) {
                log.warn("Spam detected: Email {} sent message within {} minutes",
                        email, SPAM_PROTECTION_MINUTES);
                throw new BusinessException(
                        HttpStatus.TOO_MANY_REQUESTS,
                        String.format("You have recently sent a message. Please wait %d minutes before sending again.",
                                SPAM_PROTECTION_MINUTES)
                );
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Không thể kiểm tra spam protection: {}", e.getMessage());
        }
    }

    private void sendEmailNotificationAsync(ContactMessage contactMessage) {
        try {
            brevoEmailService.sendContactNotificationAsync(contactMessage);
            log.debug("Đã trigger gửi email async cho tin nhắn ID: {}", contactMessage.getId());
        } catch (Exception e) {
            log.error("Lỗi khi trigger gửi email: {}", e.getMessage());
        }
    }
}

