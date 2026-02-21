package com.linhnguyen.portfolio_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Cấu hình CORS (Cross-Origin Resource Sharing) cho phép các request từ domain khác.
 * Quan trọng để frontend applications có thể giao tiếp với API này.
 *
 * CORS là cơ chế bảo mật của trình duyệt, ngăn chặn các request từ domain không được phép.
 */
@Configuration
public class CorsConfig {

    /**
     * Tạo CorsFilter với các thiết lập cho phép frontend gọi API.
     *
     * @return CorsFilter được cấu hình
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Danh sách các origin được phép (cập nhật theo URL frontend)
        corsConfiguration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",      // Vite development server
                "https://linhnguyen.dev"      // Frontend production
        ));

        // Các HTTP methods được phép
        corsConfiguration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        // Cho phép tất cả các headers
        corsConfiguration.setAllowedHeaders(List.of("*"));

        // Cho phép gửi credentials (cookies, authorization headers)
        corsConfiguration.setAllowCredentials(true);

        // Thời gian browser cache CORS response (1 giờ = 3600 giây)
        corsConfiguration.setMaxAge(3600L);

        // Áp dụng cấu hình CORS cho tất cả các endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}
