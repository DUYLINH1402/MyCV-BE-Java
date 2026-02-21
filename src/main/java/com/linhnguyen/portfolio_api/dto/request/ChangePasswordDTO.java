package com.linhnguyen.portfolio_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO cho yêu cầu đổi mật khẩu.
 * Admin có thể đổi mật khẩu thông qua API này.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin đổi mật khẩu")
public class ChangePasswordDTO {

    @Schema(description = "Mật khẩu hiện tại", example = "oldPassword123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Mật khẩu hiện tại không được để trống")
    private String currentPassword;

    @Schema(description = "Mật khẩu mới (6-100 ký tự)", example = "newPassword456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu mới phải từ 6-100 ký tự")
    private String newPassword;

    @Schema(description = "Xác nhận mật khẩu mới (phải trùng với mật khẩu mới)", example = "newPassword456", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
}

