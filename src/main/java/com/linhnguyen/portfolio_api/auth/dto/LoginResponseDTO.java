package com.linhnguyen.portfolio_api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO cho response sau khi đăng nhập thành công.
 * Trả về JWT token để client sử dụng cho các request tiếp theo.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Successful login response")
public class LoginResponseDTO {

    @Schema(description = "JWT Access Token. Send this token in the Authorization header: Bearer <token>",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "Token type (always Bearer)", example = "Bearer")
    @JsonProperty("token_type")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Token expiration time (in seconds)", example = "86400")
    @JsonProperty("expires_in")
    private Long expiresIn;

    @Schema(description = "Full name of the authenticated user", example = "Nguyen Duy Linh")
    @JsonProperty("full_name")
    private String fullName;
}

