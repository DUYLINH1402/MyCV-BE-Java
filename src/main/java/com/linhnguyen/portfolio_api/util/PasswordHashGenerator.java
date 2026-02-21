package com.linhnguyen.portfolio_api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class để generate BCrypt password hash.
 * Chỉ dùng cho mục đích development/testing.
 * Chạy: mvn exec:java -Dexec.mainClass="com.linhnguyen.portfolio_api.util.PasswordHashGenerator"
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = args.length > 0 ? args[0] : "admin123";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("Raw Password: " + rawPassword);
        System.out.println("BCrypt Hash: " + encodedPassword);
        System.out.println("\nVerify: " + encoder.matches(rawPassword, encodedPassword));
    }
}

