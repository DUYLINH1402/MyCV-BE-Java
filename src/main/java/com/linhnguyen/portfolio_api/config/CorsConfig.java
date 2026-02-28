package com.linhnguyen.portfolio_api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Cấu hình CORS (Cross-Origin Resource Sharing) cho phép các request từ domain khác.
 * Đọc cấu hình từ application.yml thông qua {@link CorsProperties}.
 *
 * Tích hợp trực tiếp với Spring Security thông qua {@link CorsConfigurationSource}
 * để đảm bảo CORS được xử lý đúng thứ tự trong filter chain.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final CorsProperties corsProperties;

    /**
     * Tạo CorsConfigurationSource để tích hợp với Spring Security.
     * Cấu hình được đọc từ application.yml, hỗ trợ override theo profile.
     *
     * @return CorsConfigurationSource đã cấu hình
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Đọc danh sách origin từ properties (phân tách bởi dấu phẩy)
        List<String> origins = Arrays.asList(corsProperties.getAllowedOrigins().split(","));
        configuration.setAllowedOrigins(origins);
        log.info("CORS - Allowed Origins: {}", origins);

        // Đọc danh sách HTTP methods từ properties
        List<String> methods = Arrays.asList(corsProperties.getAllowedMethods().split(","));
        configuration.setAllowedMethods(methods);

        // Đọc danh sách headers từ properties
        List<String> headers = Arrays.asList(corsProperties.getAllowedHeaders().split(","));
        configuration.setAllowedHeaders(headers);

        // Đọc danh sách exposed headers từ properties
        if (corsProperties.getExposedHeaders() != null && !corsProperties.getExposedHeaders().isEmpty()) {
            List<String> exposedHeaders = Arrays.asList(corsProperties.getExposedHeaders().split(","));
            configuration.setExposedHeaders(exposedHeaders);
        }

        // Cho phép gửi credentials (cookies, authorization headers)
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());

        // Thời gian browser cache CORS preflight response
        configuration.setMaxAge(corsProperties.getMaxAge());

        // Áp dụng cấu hình CORS cho tất cả các endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
