package com.linhnguyen.portfolio_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Đọc cấu hình CORS từ application.yml (prefix: app.cors).
 * Hỗ trợ override theo profile (dev/prod) và biến môi trường.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    /**
     * Danh sách origin được phép, phân tách bởi dấu phẩy.
     * Ví dụ: "http://localhost:5173,http://localhost:3000"
     */
    private String allowedOrigins;

    /**
     * Danh sách HTTP methods được phép.
     */
    private String allowedMethods;

    /**
     * Danh sách headers được phép.
     */
    private String allowedHeaders;

    /**
     * Danh sách headers được expose cho client.
     */
    private String exposedHeaders;

    /**
     * Cho phép gửi credentials (cookies, authorization headers).
     */
    private boolean allowCredentials;

    /**
     * Thời gian browser cache CORS preflight response (giây).
     */
    private long maxAge;
}

