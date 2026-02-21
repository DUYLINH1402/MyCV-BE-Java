package com.linhnguyen.portfolio_api.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cấu hình Cache sử dụng Caffeine cho local caching.
 * Caffeine là thư viện cache hiệu năng cao, phù hợp cho môi trường development và single-instance.
 *
 * Có thể mở rộng để sử dụng Redis cho distributed caching trong môi trường production.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cấu hình Caffeine Cache Manager với các thiết lập mặc định:
     * - Tối đa 500 entries trong cache
     * - Tự động hết hạn sau 10 phút kể từ khi ghi
     * - Ghi lại thống kê để theo dõi hiệu suất cache
     *
     * @return CacheManager được cấu hình sẵn
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());

        // Đăng ký tên các cache được sử dụng trong hệ thống
        cacheManager.setCacheNames(java.util.List.of(
                "profile",      // Cache cho thông tin profile
                "projects",   // Cache cho thông tin project
                "skills"      // Cache cho thông tin skill
        ));

        return cacheManager;
    }
}
