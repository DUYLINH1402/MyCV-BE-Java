package com.linhnguyen.portfolio_api.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception được ném ra khi không tìm thấy resource được yêu cầu.
 * Trả về HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends BusinessException {

    /**
     * Constructor với message tùy chỉnh.
     *
     * @param message Thông báo lỗi
     */
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
    }

    /**
     * Constructor với tên resource và ID.
     * Tự động tạo message dạng: "{resourceName} not found with id: {id}"
     *
     * @param resourceName Tên của resource (ví dụ: Profile, Project)
     * @param id           ID của resource không tìm thấy
     */
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id),
              HttpStatus.NOT_FOUND,
              "RESOURCE_NOT_FOUND");
    }

    /**
     * Constructor với tên resource, tên field và giá trị field.
     * Tự động tạo message dạng: "{resourceName} not found with {fieldName}: {fieldValue}"
     *
     * @param resourceName Tên của resource
     * @param fieldName    Tên của field dùng để tìm kiếm
     * @param fieldValue   Giá trị của field
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue),
              HttpStatus.NOT_FOUND,
              "RESOURCE_NOT_FOUND");
    }
}
