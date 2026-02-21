package com.linhnguyen.portfolio_api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Định dạng response chuẩn cho tất cả các lỗi API.
 * Đảm bảo client nhận được thông tin lỗi nhất quán và dễ xử lý.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /** Thời điểm xảy ra lỗi */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /** HTTP status code */
    private int status;

    /** Tên lỗi HTTP (ví dụ: Bad Request, Not Found) */
    private String error;

    /** Mã lỗi nội bộ để client xử lý theo từng loại */
    private String errorCode;

    /** Thông báo lỗi chi tiết */
    private String message;

    /** Đường dẫn API gây ra lỗi */
    private String path;

    /** Map chứa các lỗi validation theo từng field (chỉ có khi validation thất bại) */
    private Map<String, String> validationErrors;
}
