package com.linhnguyen.portfolio_api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Lớp wrapper chung cho tất cả API Response.
 * Đảm bảo định dạng response thống nhất trong toàn bộ hệ thống.
 *
 * @param <T> Kiểu dữ liệu của data trả về
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response chuẩn cho tất cả API")
public class ApiResponse<T> {

    @Schema(description = "HTTP status code", example = "200")
    private int status;

    @Schema(description = "Thông báo kết quả", example = "Success")
    private String message;

    @Schema(description = "Dữ liệu trả về")
    private T data;

    @Schema(description = "Thời điểm xử lý request", example = "2025-01-11T10:30:00")
    private LocalDateTime timestamp;

    /**
     * Constructor riêng để tạo response với timestamp tự động.
     *
     * @param status  HTTP status code
     * @param message Thông báo
     * @param data    Dữ liệu trả về
     */
    private ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Tạo response thành công kèm dữ liệu.
     *
     * @param data Dữ liệu trả về cho client
     * @return ApiResponse với status 200 và data
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    /**
     * Tạo response thành công với message tùy chỉnh.
     *
     * @param message Thông báo tùy chỉnh
     * @param data    Dữ liệu trả về cho client
     * @return ApiResponse với status 200, message và data
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    /**
     * Tạo response cho việc tạo mới thành công (HTTP 201).
     *
     * @param data Dữ liệu của resource vừa được tạo
     * @return ApiResponse với status 201
     */
    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "Created successfully", data);
    }

    /**
     * Tạo response lỗi.
     *
     * @param status  HTTP status code
     * @param message Thông báo lỗi
     * @return ApiResponse với thông tin lỗi
     */
    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}
