package com.linhnguyen.portfolio_api.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.request.ContactRequestDTO;
import com.linhnguyen.portfolio_api.dto.response.ContactResponseDTO;
import com.linhnguyen.portfolio_api.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller xử lý API liên hệ (Contact).
 * Cho phép nhà tuyển dụng gửi tin nhắn liên hệ đến chủ portfolio.
 *
 * API công khai, không yêu cầu authentication.
 */
@RestController
@RequestMapping("/v1/public/contact")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Contact", description = "Public API for sending contact messages to the portfolio owner")
public class ContactController {

    private final ContactService contactService;

    /**
     * Gửi tin nhắn liên hệ.
     *
     * Endpoint công khai cho phép nhà tuyển dụng gửi tin nhắn liên hệ.
     * Tin nhắn sẽ được lưu vào database và gửi email thông báo đến chủ portfolio.
     *
     * @param requestDTO Thông tin tin nhắn liên hệ
     * @param request    HttpServletRequest để lấy IP client
     * @return Response xác nhận gửi thành công
     */
    @PostMapping
    @Operation(
            summary = "Send a contact message",
            description = "Allows recruiters to send a contact message to the portfolio owner. " +
                    "The message will be saved and an email notification will be sent."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Message sent successfully",
                    content = @Content(schema = @Schema(implementation = ContactResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "429",
                    description = "Too many requests (spam protection)"
            )
    })
    public ResponseEntity<ApiResponse<ContactResponseDTO>> submitContact(
            @Valid @RequestBody ContactRequestDTO requestDTO,
            HttpServletRequest request
    ) {
        String clientIp = getClientIp(request);
        log.info("Received contact message request from IP: {}", clientIp);

        ContactResponseDTO response = contactService.submitContactMessage(requestDTO, clientIp);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Message sent successfully", response));
    }

    /**
     * Lấy IP thực của client, hỗ trợ cả trường hợp có proxy/load balancer.
     *
     * @param request HttpServletRequest
     * @return IP address của client
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // X-Forwarded-For may contain multiple IPs, take the first one (actual client)
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
