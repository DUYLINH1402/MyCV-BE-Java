package com.linhnguyen.portfolio_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filter xử lý JWT Authentication.
 * Được thực thi trước mỗi request để kiểm tra và xác thực JWT token.
 * Nếu token hợp lệ, set Authentication vào SecurityContext.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // Lấy JWT token từ request header
            String jwt = getJwtFromRequest(request);

            // Nếu có token và token hợp lệ
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // Lấy username từ token
                String username = jwtTokenProvider.extractUsername(jwt);

                // Tạo Authentication object với quyền ADMIN
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set Authentication vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Set Authentication cho user: {}", username);
            }
        } catch (Exception e) {
            log.error("Không thể set user authentication: {}", e.getMessage());
        }

        // Tiếp tục filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Lấy JWT token từ Authorization header.
     * Format: "Bearer <token>"
     *
     * @param request HttpServletRequest
     * @return JWT token string hoặc null nếu không tìm thấy
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    /**
     * Bỏ qua filter cho các endpoint không cần authenticate.
     * Tối ưu performance bằng cách skip filter cho các public endpoints.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // Không filter cho các endpoint public
        return path.startsWith("/v1/auth/") ||
               path.startsWith("/v1/health") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs");
    }
}

