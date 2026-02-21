package com.linhnguyen.portfolio_api.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller kiểm tra trạng thái hoạt động của API.
 * Dùng cho monitoring và health check từ các hệ thống bên ngoài.
 */
@RestController
@RequestMapping("/v1/health")
@Slf4j
@Tag(name = "Health", description = "API kiểm tra trạng thái hệ thống")
public class HealthController {

    /**
     * Kiểm tra trạng thái hoạt động của API.
     * Endpoint này nên luôn trả về thành công nếu service đang chạy.
     *
     * @return Thông tin trạng thái service bao gồm: status, timestamp, service name, version
     */
    @GetMapping
    @Operation(summary = "Health check", description = "Kiểm tra xem API có đang hoạt động không")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
//        log.debug("Request kiểm tra health");

        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", LocalDateTime.now());
        healthInfo.put("service", "Portfolio API");
        healthInfo.put("version", "1.0.0");

        return ResponseEntity.ok(ApiResponse.success("Service đang hoạt động bình thường", healthInfo));
    }
}
