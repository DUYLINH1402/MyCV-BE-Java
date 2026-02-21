package com.linhnguyen.portfolio_api.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.request.ChangePasswordDTO;
import com.linhnguyen.portfolio_api.dto.request.LoginRequestDTO;
import com.linhnguyen.portfolio_api.dto.response.LoginResponseDTO;
import com.linhnguyen.portfolio_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller xử lý Authentication.
 *
 * Chỉ có endpoint login, không có register vì đây là portfolio cá nhân.
 * Endpoint này được giấu khỏi người dùng thông thường trên frontend.
 * Admin sử dụng URL trực tiếp hoặc giao diện ẩn để đăng nhập.
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "API xác thực dành cho Admin (Endpoint ẩn)")
public class AuthController {

    private final AuthService authService;

    /**
     * Đăng nhập Admin.
     * Trả về JWT token nếu thông tin đăng nhập hợp lệ.
     *
     * Lưu ý: Endpoint này nên được giấu trên frontend để tránh nhà tuyển dụng nhìn thấy.
     *
     * @param request DTO chứa email và password
     * @return JWT token và thông tin liên quan
     */
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập Admin",
               description = "Xác thực Admin bằng email và password, trả về JWT token. Endpoint này được ẩn trên giao diện người dùng.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Đăng nhập thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Thông tin đăng nhập không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Email hoặc mật khẩu không đúng")
    })
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        log.info("Login request received");
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", response));
    }

    /**
     * Đổi mật khẩu Admin.
     * Yêu cầu xác thực JWT token và nhập đúng mật khẩu hiện tại.
     *
     * @param request DTO chứa mật khẩu hiện tại và mật khẩu mới
     * @return Thông báo thành công
     */
    @PutMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu",
               description = "Admin đổi mật khẩu. Yêu cầu xác thực JWT token.")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Đổi mật khẩu thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc mật khẩu xác nhận không khớp"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ")
    })
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordDTO request) {
        log.info("Change password request received");
        authService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công", null));
    }
}

