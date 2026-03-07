package com.linhnguyen.portfolio_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Cấu hình Async cho Spring Boot.
 * Cho phép chạy các method với @Async annotation bất đồng bộ.
 *
 * Sử dụng cho: Gửi email qua Brevo không block main thread.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // Spring Boot sẽ tự động cấu hình ThreadPoolTaskExecutor
    // Có thể customize nếu cần bằng cách định nghĩa Executor bean
}

