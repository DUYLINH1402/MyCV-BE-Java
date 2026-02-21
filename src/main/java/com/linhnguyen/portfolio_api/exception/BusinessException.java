package com.linhnguyen.portfolio_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Lớp Exception cơ sở cho tất cả các lỗi nghiệp vụ trong hệ thống.
 * Các exception cụ thể nên kế thừa từ lớp này để đảm bảo tính nhất quán trong xử lý lỗi.
 */
@Getter
public class BusinessException extends RuntimeException {

    /** HTTP status code trả về cho client */
    private final HttpStatus status;

    /** Mã lỗi để client có thể xử lý theo từng loại lỗi */
    private final String errorCode;

    /**
     * Constructor với message mặc định status 400 Bad Request.
     *
     * @param message Thông báo lỗi
     */
    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = "BUSINESS_ERROR";
    }

    /**
     * Constructor với message và status tùy chỉnh.
     *
     * @param message Thông báo lỗi
     * @param status  HTTP status code
     */
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = "BUSINESS_ERROR";
    }

    /**
     * Constructor với status và message (thứ tự ngược).
     * Tiện lợi cho các trường hợp muốn chỉ định status trước.
     *
     * @param status  HTTP status code
     * @param message Thông báo lỗi
     */
    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.errorCode = "BUSINESS_ERROR";
    }

    /**
     * Constructor đầy đủ với message, status và error code.
     *
     * @param message   Thông báo lỗi
     * @param status    HTTP status code
     * @param errorCode Mã lỗi
     */
    public BusinessException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
