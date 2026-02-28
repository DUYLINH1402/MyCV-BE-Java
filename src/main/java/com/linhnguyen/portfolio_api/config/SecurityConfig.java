package com.linhnguyen.portfolio_api.config;

import com.linhnguyen.portfolio_api.security.JwtAuthenticationEntryPoint;
import com.linhnguyen.portfolio_api.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Cấu hình Spring Security cho Portfolio API.
 *
 * Phân quyền:
 * - Public endpoints: GET /v1/profile, /v1/projects, /v1/skills (không cần xác thực)
 * - Admin endpoints: /v1/admin/** (yêu cầu JWT token)
 * - Auth endpoints: /v1/auth/** (public cho login)
 *
 * Sử dụng Stateless Session vì xác thực bằng JWT.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * Các endpoint hoàn toàn public: không cần xác thực bất kể HTTP method.
     * Bao gồm: auth, health check, Swagger UI docs.
     */
    private static final String[] PUBLIC_ENDPOINTS = {
            "/v1/auth/login",
            "/v1/health/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    /**
     * Các endpoint Portfolio công khai — chỉ cho phép đọc (GET).
     * Frontend dùng để hiển thị thông tin, không cần đăng nhập.
     */
    private static final String[] PUBLIC_GET_ENDPOINTS = {
            "/v1/profile/**",
            "/v1/projects/**",
            "/v1/skills/**"
    };

    /**
     * Các endpoint quản trị — yêu cầu JWT với role ADMIN.
     */
    private static final String[] ADMIN_ENDPOINTS = {
            "/v1/admin/**"
    };

    /**
     * Cấu hình SecurityFilterChain.
     * Định nghĩa các quy tắc phân quyền và xác thực cho từng endpoint.
     *
     * @param http HttpSecurity builder
     * @return SecurityFilterChain đã cấu hình
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tích hợp CORS với Spring Security - đảm bảo CORS filter chạy trước các security filter
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Disable CSRF vì sử dụng JWT (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // Cấu hình exception handling
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                // Stateless session - không lưu session trên server
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Cấu hình phân quyền cho các endpoints
                .authorizeHttpRequests(auth -> auth
                        // Permit toàn bộ OPTIONS request — cần thiết cho CORS preflight
                        // Trình duyệt gửi OPTIONS trước mọi cross-origin request có credentials
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Các endpoint công khai: auth, health, swagger docs
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        // Portfolio public APIs: chỉ cho phép đọc (GET), không cần xác thực
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()

                        // Admin APIs: yêu cầu JWT hợp lệ với role ADMIN
                        .requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")

                        // Tất cả request còn lại đều yêu cầu xác thực
                        .anyRequest().authenticated()
                )

                // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Password Encoder sử dụng BCrypt.
     * BCrypt tự động salt và có strength factor để tăng độ bảo mật.
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

