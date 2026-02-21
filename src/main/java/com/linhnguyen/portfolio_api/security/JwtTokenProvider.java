package com.linhnguyen.portfolio_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class để xử lý JWT Token.
 * Bao gồm: tạo token, validate token, và extract thông tin từ token.
 * Sử dụng thuật toán HS256 với secret key từ cấu hình.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Tạo JWT Token cho username.
     *
     * @param username Tên đăng nhập của admin
     * @return JWT Token string
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Lấy username từ JWT Token.
     *
     * @param token JWT Token
     * @return Username được extract từ token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Lấy thời gian hết hạn từ JWT Token.
     *
     * @param token JWT Token
     * @return Thời gian hết hạn
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Trích xuất một claim cụ thể từ token.
     *
     * @param token          JWT Token
     * @param claimsResolver Function để resolve claim
     * @return Giá trị của claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Validate JWT Token.
     * Kiểm tra token có hợp lệ và chưa hết hạn không.
     *
     * @param token    JWT Token
     * @param username Username cần kiểm tra
     * @return true nếu token hợp lệ
     */
    public boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Validate token mà không cần username.
     * Sử dụng cho filter để kiểm tra token có đúng format không.
     *
     * @param token JWT Token
     * @return true nếu token hợp lệ về mặt cấu trúc và chưa hết hạn
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Kiểm tra token đã hết hạn chưa.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Lấy tất cả claims từ token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Tạo SecretKey từ chuỗi secret đã cấu hình.
     * Sử dụng Base64 decode nếu key đã được encode, hoặc dùng trực tiếp nếu không.
     */
    private SecretKey getSigningKey() {
        try {
            // Thử decode Base64 trước
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            // Nếu không phải Base64, sử dụng trực tiếp bytes của string
            return Keys.hmacShaKeyFor(secretKey.getBytes());
        }
    }
}

