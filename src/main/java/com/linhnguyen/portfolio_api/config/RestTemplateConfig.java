package com.linhnguyen.portfolio_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Cấu hình RestTemplate cho các HTTP calls đến external services (Brevo API).
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Tạo RestTemplate bean với timeout configuration.
     *
     * @return RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // Timeout 10 giây cho connection
        factory.setConnectTimeout(10000);
        // Timeout 30 giây cho read
        factory.setReadTimeout(30000);

        return new RestTemplate(factory);
    }
}

