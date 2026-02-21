package com.linhnguyen.portfolio_api.dto.response;

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
@Schema(description = "Response sau khi đăng nhập thành công")
public class LoginResponseDTO {

    @Schema(description = "JWT Access Token. Client gửi token này trong header Authorization: Bearer <token>",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "Loại token (luôn là Bearer)", example = "Bearer")
    @JsonProperty("token_type")
    @Builder.Default
    private String tokenType = "Bearer";

    @Schema(description = "Thời gian token hết hạn (tính bằng giây)", example = "86400")
    @JsonProperty("expires_in")
    private Long expiresIn;

    @Schema(description = "Tên đầy đủ của người đăng nhập", example = "Nguyễn Duy Linh")
    @JsonProperty("full_name")
    private String fullName;
}

