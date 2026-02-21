package com.linhnguyen.portfolio_api.service;

import com.linhnguyen.portfolio_api.dto.request.ChangePasswordDTO;
import com.linhnguyen.portfolio_api.dto.request.LoginRequestDTO;
import com.linhnguyen.portfolio_api.dto.response.LoginResponseDTO;
import com.linhnguyen.portfolio_api.entity.AdminCredential;
import com.linhnguyen.portfolio_api.exception.BusinessException;
import com.linhnguyen.portfolio_api.repository.AdminCredentialRepository;
import com.linhnguyen.portfolio_api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service xử lý Authentication cho Admin.
 *
 * Sử dụng email và password từ bảng admin_credentials để xác thực.
 * Portfolio chỉ có 1 owner duy nhất, thông tin đăng nhập được lưu riêng biệt với Profile.
 * Không có chức năng đăng ký vì đây là portfolio cá nhân.
 * Mật khẩu được lưu dưới dạng BCrypt hash.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AdminCredentialRepository adminCredentialRepository;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Xử lý đăng nhập Admin.
     * Kiểm tra email và password từ bảng admin_credentials, nếu hợp lệ trả về JWT token.
     *
     * @param request DTO chứa thông tin đăng nhập (email, password)
     * @return LoginResponseDTO chứa JWT token
     * @throws BusinessException nếu thông tin đăng nhập không hợp lệ
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Attempting login for email: {}", request.getEmail());

        // Tìm admin credential theo email
        AdminCredential credential = adminCredentialRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: Email not found - {}", request.getEmail());
                    return new BusinessException(HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không đúng");
                });

        // Kiểm tra password đã được set chưa
        if (credential.getPassword() == null || credential.getPassword().isBlank()) {
            log.error("Login failed: Password not set for email - {}", request.getEmail());
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Tài khoản chưa được thiết lập mật khẩu");
        }

        // Kiểm tra password
        boolean passwordMatch = passwordEncoder.matches(request.getPassword(), credential.getPassword());

        if (!passwordMatch) {
            log.warn("Login failed: Invalid password for email - {}", request.getEmail());
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Email hoặc mật khẩu không đúng");
        }

        // Tạo JWT token với email làm subject
        String token = jwtTokenProvider.generateToken(credential.getEmail());

        log.info("Login successful for email: {}", request.getEmail());

        // Trả về response với token
        return LoginResponseDTO.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration / 1000) // Chuyển từ ms sang giây
                .fullName(credential.getEmail()) // Dùng email vì không có fullName trong credential
                .build();
    }

    /**
     * Kiểm tra token có hợp lệ không.
     * Dùng để verify token khi cần.
     *
     * @param token JWT Token cần kiểm tra
     * @return true nếu token hợp lệ
     */
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    /**
     * Đổi mật khẩu cho admin.
     * Yêu cầu nhập đúng mật khẩu hiện tại để xác thực.
     *
     * @param request DTO chứa thông tin đổi mật khẩu
     * @throws BusinessException nếu mật khẩu hiện tại không đúng hoặc xác nhận không khớp
     */
    @Transactional
    public void changePassword(ChangePasswordDTO request) {
        log.info("Attempting to change password");

        // Lấy admin credential
        AdminCredential credential = adminCredentialRepository.findFirstByIsActiveTrue()
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Admin credential không tồn tại"));

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(request.getCurrentPassword(), credential.getPassword())) {
            log.warn("Change password failed: Current password is incorrect");
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Mật khẩu hiện tại không đúng");
        }

        // Kiểm tra xác nhận mật khẩu mới
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            log.warn("Change password failed: New password confirmation does not match");
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Xác nhận mật khẩu không khớp");
        }

        // Kiểm tra mật khẩu mới không trùng mật khẩu cũ
        if (passwordEncoder.matches(request.getNewPassword(), credential.getPassword())) {
            log.warn("Change password failed: New password same as current");
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Mật khẩu mới không được trùng với mật khẩu hiện tại");
        }

        // Cập nhật mật khẩu mới (BCrypt encoded)
        credential.setPassword(passwordEncoder.encode(request.getNewPassword()));
        adminCredentialRepository.save(credential);

        log.info("Password changed successfully");
    }
}

