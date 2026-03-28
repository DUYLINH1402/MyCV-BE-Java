package com.linhnguyen.portfolio_api.auth.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.auth.dto.LoginRequestDTO;
import com.linhnguyen.portfolio_api.auth.dto.LoginResponseDTO;
import com.linhnguyen.portfolio_api.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller xử lý Authentication (Public).
 *
 * Chỉ có endpoint login, không có register vì đây là portfolio cá nhân.
 * Endpoint này được giấu khỏi người dùng thông thường trên frontend.
 * Admin sử dụng URL trực tiếp hoặc giao diện ẩn để đăng nhập.
 */
@RestController
@RequestMapping("/v1/public/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication API for Admin (Hidden endpoint)")
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
    @Operation(summary = "Admin Login",
               description = "Authenticate Admin with email and password, returns a JWT token. This endpoint is hidden on the user interface.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid login credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Incorrect email or password")
    })
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        log.info("Login request received");
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
}

