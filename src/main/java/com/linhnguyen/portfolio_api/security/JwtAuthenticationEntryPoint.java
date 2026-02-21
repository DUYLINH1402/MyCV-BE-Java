package com.linhnguyen.portfolio_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linhnguyen.portfolio_api.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Entry point xử lý khi có lỗi Authentication.
 * Trả về JSON response thay vì redirect hoặc HTML error page.
 * Đảm bảo API luôn trả về format JSON chuẩn.
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.error("Unauthorized error: {} - Path: {}", authException.getMessage(), request.getRequestURI());

        // Thiết lập response headers
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Tạo error response JSON chuẩn
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message("Bạn cần đăng nhập để truy cập tài nguyên này")
                .path(request.getRequestURI())
                .build();

        // Ghi response
        objectMapper.findAndRegisterModules(); // Support LocalDateTime
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}

