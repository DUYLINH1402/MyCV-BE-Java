package com.linhnguyen.portfolio_api.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception được ném ra khi phát hiện resource bị trùng lặp.
 * Ví dụ: Tạo user với email đã tồn tại, tạo project với title đã có.
 * Trả về HTTP 409 Conflict.
 */
public class DuplicateResourceException extends BusinessException {

    /**
     * Constructor với message tùy chỉnh.
     *
     * @param message Thông báo lỗi
     */
    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT, "DUPLICATE_RESOURCE");
    }

    /**
     * Constructor với tên resource, tên field và giá trị field.
     * Tự động tạo message dạng: "{resourceName} already exists with {fieldName}: {fieldValue}"
     *
     * @param resourceName Tên của resource (ví dụ: Profile, Project)
     * @param fieldName    Tên của field bị trùng
     * @param fieldValue   Giá trị bị trùng
     */
    public DuplicateResourceException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s already exists with %s: %s", resourceName, fieldName, fieldValue),
              HttpStatus.CONFLICT,
              "DUPLICATE_RESOURCE");
    }
}
