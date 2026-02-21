package com.linhnguyen.portfolio_api.exception.handler;

import com.linhnguyen.portfolio_api.exception.BusinessException;
import com.linhnguyen.portfolio_api.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * Xử lý exception toàn cục cho tất cả các Controller.
 * Đảm bảo định dạng response lỗi nhất quán trong toàn bộ API.
 * Sử dụng @RestControllerAdvice để tự động bắt và xử lý các exception.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Xử lý tất cả BusinessException và các lớp con của nó.
     * Bao gồm: ResourceNotFoundException, DuplicateResourceException, v.v.
     *
     * @param ex      Exception được ném ra
     * @param request HttpServletRequest để lấy thông tin đường dẫn
     * @return ResponseEntity chứa thông tin lỗi
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        log.warn("Lỗi nghiệp vụ xảy ra: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ex.getStatus().value())
                .error(ex.getStatus().getReasonPhrase())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    /**
     * Xử lý lỗi validation từ annotation @Valid.
     * Trích xuất tất cả các lỗi validation và trả về dưới dạng map (fieldName -> errorMessage).
     *
     * @param ex      Exception chứa thông tin validation errors
     * @param request HttpServletRequest để lấy thông tin đường dẫn
     * @return ResponseEntity chứa danh sách lỗi validation theo từng field
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.warn("Validation thất bại: {}", ex.getMessage());

        // Trích xuất lỗi validation cho từng field
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errorCode("VALIDATION_ERROR")
                .message("Validation failed")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Xử lý lỗi khi kiểu dữ liệu không khớp.
     * Ví dụ: Truyền string "abc" cho tham số ID kiểu Long.
     *
     * @param ex      Exception chứa thông tin lỗi kiểu dữ liệu
     * @param request HttpServletRequest để lấy thông tin đường dẫn
     * @return ResponseEntity chứa thông tin lỗi
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        log.warn("Lỗi kiểu dữ liệu: {}", ex.getMessage());

        String message = String.format("Invalid value '%s' for parameter '%s'",
                ex.getValue(), ex.getName());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errorCode("TYPE_MISMATCH")
                .message(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Xử lý tất cả các exception không được xử lý bởi các handler khác.
     * Đây là handler cuối cùng để đảm bảo không có exception nào bị lọt ra ngoài.
     * Log đầy đủ stack trace để debug.
     *
     * @param ex      Exception không xác định
     * @param request HttpServletRequest để lấy thông tin đường dẫn
     * @return ResponseEntity với status 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Lỗi không mong muốn xảy ra: ", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .errorCode("INTERNAL_ERROR")
                .message("An unexpected error occurred. Please try again later.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
