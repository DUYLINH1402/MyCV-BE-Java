package com.linhnguyen.portfolio_api.contact.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.contact.dto.ContactRequestDTO;
import com.linhnguyen.portfolio_api.contact.dto.ContactResponseDTO;
import com.linhnguyen.portfolio_api.contact.service.ContactService;
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
 */
@RestController
@RequestMapping("/v1/public/contact")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Contact", description = "Public API for sending contact messages to the portfolio owner")
public class ContactController {

    private final ContactService contactService;

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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "429", description = "Too many requests (spam protection)")
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

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}

