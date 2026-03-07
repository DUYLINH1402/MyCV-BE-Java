package com.linhnguyen.portfolio_api.service;

import com.linhnguyen.portfolio_api.dto.request.ContactRequestDTO;
import com.linhnguyen.portfolio_api.dto.response.ContactResponseDTO;
import com.linhnguyen.portfolio_api.entity.ContactMessage;
import com.linhnguyen.portfolio_api.exception.BusinessException;
import com.linhnguyen.portfolio_api.mapper.ContactMessageMapper;
import com.linhnguyen.portfolio_api.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service xử lý logic nghiệp vụ cho module Contact.
 * Bao gồm: nhận tin nhắn từ nhà tuyển dụng, lưu DB, gửi email thông báo.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;
    private final BrevoEmailService brevoEmailService;

    /**
     * Số phút tối thiểu giữa 2 lần gửi từ cùng 1 email (chống spam).
     */
    private static final int SPAM_PROTECTION_MINUTES = 5;

    /**
     * Xử lý request gửi tin nhắn liên hệ.
     *
     * Flow:
     * 1. Kiểm tra spam (cùng email gửi nhiều lần trong thời gian ngắn)
     * 2. Lưu tin nhắn vào database
     * 3. Gửi email thông báo qua Brevo (async)
     * 4. Trả về response xác nhận
     *
     * @param requestDTO Request chứa thông tin liên hệ
     * @param clientIp   IP của client (để tracking)
     * @return Response xác nhận đã gửi thành công
     */
    @Transactional
    public ContactResponseDTO submitContactMessage(ContactRequestDTO requestDTO, String clientIp) {
        log.info("Nhận tin nhắn liên hệ từ: {} <{}>", requestDTO.getName(), requestDTO.getEmail());

        // Kiểm tra spam
        checkSpamProtection(requestDTO.getEmail());

        // Map DTO sang Entity
        ContactMessage contactMessage = contactMessageMapper.toEntity(requestDTO);
        contactMessage.setSenderIp(clientIp);

        // Lưu vào database
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        log.info("Đã lưu tin nhắn liên hệ. ID: {}", savedMessage.getId());

        // Gửi email thông báo (async - không block request)
        sendEmailNotificationAsync(savedMessage);

        // Tạo response
        ContactResponseDTO responseDTO = contactMessageMapper.toResponseDTO(savedMessage);
        responseDTO.setConfirmationMessage(
                "Thank you for reaching out! Your message has been sent successfully. " +
                "I will respond to your email as soon as possible."
        );

        return responseDTO;
    }

    /**
     * Kiểm tra spam protection.
     * Nếu email đã gửi tin nhắn trong vòng SPAM_PROTECTION_MINUTES phút gần đây -> từ chối.
     *
     * @param email Email cần kiểm tra
     */
    private void checkSpamProtection(String email) {
        try {
            // Tính thời gian cutoff = hiện tại - số phút spam protection
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
            // Log lỗi nhưng không block request nếu query thất bại
            log.warn("Không thể kiểm tra spam protection: {}", e.getMessage());
        }
    }

    /**
     * Gửi email thông báo (async).
     * Chạy bất đồng bộ để không ảnh hưởng response time của API.
     *
     * @param contactMessage Tin nhắn liên hệ
     */
    private void sendEmailNotificationAsync(ContactMessage contactMessage) {
        try {
            brevoEmailService.sendContactNotificationAsync(contactMessage);
            log.debug("Đã trigger gửi email async cho tin nhắn ID: {}", contactMessage.getId());
        } catch (Exception e) {
            // Log lỗi nhưng không throw để user vẫn nhận được response thành công
            log.error("Lỗi khi trigger gửi email: {}", e.getMessage());
        }
    }
}

