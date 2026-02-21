package com.linhnguyen.portfolio_api.security;

import com.linhnguyen.portfolio_api.entity.AdminCredential;
import com.linhnguyen.portfolio_api.repository.AdminCredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service cung cấp thông tin user cho Spring Security.
 *
 * Đây là bean cần thiết để Spring Security xác thực user từ database
 * thay vì sử dụng user mặc định được sinh tự động.
 * Sử dụng bảng admin_credentials riêng biệt để quản lý thông tin đăng nhập.
 *
 * Khi có bean này, Spring sẽ không còn sinh password mặc định trong console nữa.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminCredentialRepository adminCredentialRepository;

    /**
     * Load thông tin user từ database dựa trên email.
     *
     * Method này được Spring Security gọi khi xác thực user.
     * Role của user được lấy từ trường role trong AdminCredential.
     *
     * @param email Địa chỉ email của user (dùng làm username)
     * @return UserDetails chứa thông tin xác thực
     * @throws UsernameNotFoundException Nếu không tìm thấy user với email đã cho
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Đang tìm kiếm admin credential với email: {}", email);

        AdminCredential credential = adminCredentialRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> {
                    log.warn("Không tìm thấy admin credential với email: {}", email);
                    return new UsernameNotFoundException("Admin credential not found with email: " + email);
                });

        log.debug("Tìm thấy admin credential với email: {}", email);

        // Lấy role từ AdminCredential (mặc định là ADMIN)
        String role = credential.getRole() != null ? credential.getRole() : "ADMIN";

        return new org.springframework.security.core.userdetails.User(
                credential.getEmail(),
                credential.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}

