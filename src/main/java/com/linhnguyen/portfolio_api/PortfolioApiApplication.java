package com.linhnguyen.portfolio_api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioApiApplication {

	public static void main(String[] args) {
		// Load .env file và set vào System properties TRƯỚC khi Spring khởi động
		loadDotenv();

		SpringApplication.run(PortfolioApiApplication.class, args);
	}

	/**
	 * Load biến môi trường từ file .env vào System properties.
	 * Phải được gọi TRƯỚC SpringApplication.run() để các biến có thể được resolve trong application.yml
	 */
	private static void loadDotenv() {
		try {
			Dotenv dotenv = Dotenv.configure()
					.directory("./")
					.ignoreIfMissing()
					.load();

			// Set tất cả biến từ .env vào System properties
			dotenv.entries().forEach(entry -> {
				String key = entry.getKey();
				String value = entry.getValue();

				// Chỉ set nếu chưa có trong System properties hoặc environment
				if (System.getProperty(key) == null && System.getenv(key) == null) {
					System.setProperty(key, value);
				}
			});

			System.out.println("----- Loaded " + dotenv.entries().size() + " variables from .env file");

		} catch (Exception e) {
			System.err.println("----- Could not load .env file: " + e.getMessage());
		}
	}
}
