package com.linhnguyen.portfolio_api.contact.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO cho request gửi tin nhắn liên hệ.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Contact message request from recruiters or visitors")
public class ContactRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Sender's full name", example = "John Smith", minLength = 2, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Schema(description = "Sender's email address for follow-up", example = "recruiter@company.com", maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Subject is required")
    @Size(min = 5, max = 255, message = "Subject must be between 5 and 255 characters")
    @Schema(description = "Message subject", example = "Interview Invitation for Backend Developer Position", minLength = 5, maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
    private String subject;

    @NotBlank(message = "Message content is required")
    @Size(min = 10, max = 5000, message = "Message must be between 10 and 5000 characters")
    @Schema(description = "Detailed message content", example = "Hi Linh, we are looking for a Backend Developer and your profile is a great fit...", minLength = 10, maxLength = 5000, requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}

