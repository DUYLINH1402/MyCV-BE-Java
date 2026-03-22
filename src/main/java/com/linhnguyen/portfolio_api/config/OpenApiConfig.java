package com.linhnguyen.portfolio_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Cấu hình OpenAPI (Swagger) cho tài liệu API.
 * Swagger UI giúp visualize và tương tác với các API endpoints.
 * Truy cập tại: /api/swagger-ui.html
 * Đã thêm Security Scheme cho JWT Bearer token để hỗ trợ các Admin API.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    /**
     * Cấu hình thông tin OpenAPI cho Portfolio API.
     * Bao gồm: tiêu đề, mô tả, phiên bản, thông tin liên hệ, servers và security scheme.
     *
     * @return OpenAPI object được cấu hình đầy đủ
     */
    @Bean
    public OpenAPI portfolioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NGUYEN DUY LINH Portfolio API")
                        .description("RESTful API for Personal Portfolio - Manage CV, Projects, Skills & Contact. " +
                                "Public APIs (/api/v1/public/*) do not require authentication. " +
                                "Admin APIs (/api/v1/admin/*) require a JWT Bearer token.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nguyen Duy Linh")
                                .email("duylinh63b5@gmail.com")
                                .url("duylinhcv.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort + "/api")
                                .description("Development Server (Local)"),
                        new Server()
                                .url("https://mycv-api.com/")
                                .description("Production Server")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter your JWT token to authenticate. " +
                                        "Token is issued after a successful login via /api/v1/public/auth/login")));
    }
}
