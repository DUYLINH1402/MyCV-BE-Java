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

    /**
     * Các đường dẫn công khai không cần xác thực.
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
     * Các đường dẫn API công khai (chỉ cho phép GET).
     */
    private static final String[] PUBLIC_GET_ENDPOINTS = {
            "/v1/profile/**",
            "/v1/projects/**",
            "/v1/skills/**"
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
                        // Cho phép truy cập không cần xác thực với các endpoint public
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        // Cho phép GET request đến các API công khai
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()

                        // Yêu cầu xác thực với role ADMIN cho các endpoint admin
                        .requestMatchers("/v1/admin/**").hasRole("ADMIN")

                        // Tất cả các request khác đều yêu cầu xác thực
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

