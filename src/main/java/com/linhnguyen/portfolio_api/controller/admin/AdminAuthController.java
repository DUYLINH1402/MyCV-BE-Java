package com.linhnguyen.portfolio_api.controller.admin;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.request.ChangePasswordDTO;
import com.linhnguyen.portfolio_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller xử lý các API quản lý Authentication dành cho Admin.
 * Các endpoint này yêu cầu xác thực và phân quyền Admin.
 *
 * Tách riêng khỏi AuthController (public) để đồng bộ với cấu trúc role-based URL prefix.
 */
@RestController
@RequestMapping("/v1/admin/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin - Authentication", description = "Admin authentication management API (Requires JWT authentication)")
@SecurityRequirement(name = "bearerAuth")
public class AdminAuthController {

    private final AuthService authService;

    /**
     * Đổi mật khẩu Admin.
     * Yêu cầu xác thực JWT token và nhập đúng mật khẩu hiện tại.
     *
     * @param request DTO chứa mật khẩu hiện tại và mật khẩu mới
     * @return Thông báo thành công
     */
    @PutMapping("/change-password")
    @Operation(summary = "Change Password",
               description = "Admin changes their password. Requires JWT token authentication.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid data or confirmation password does not match"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token")
    })
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordDTO request) {
        log.info("[ADMIN] Change password request received");
        authService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.successMessage("Password changed successfully"));
    }
}
