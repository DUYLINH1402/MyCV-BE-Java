-- =====================================================
-- V1__Create_contact_messages_table.sql
-- Tạo bảng lưu trữ tin nhắn liên hệ từ nhà tuyển dụng
-- =====================================================

CREATE TABLE IF NOT EXISTS contact_messages (
    id BIGSERIAL PRIMARY KEY,

    -- Thông tin người gửi
    sender_name VARCHAR(100) NOT NULL,
    sender_email VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,

    -- Trạng thái
    is_read BOOLEAN DEFAULT FALSE,
    email_sent BOOLEAN DEFAULT FALSE,

    -- Tracking
    sender_ip VARCHAR(45),

    -- Audit fields (từ BaseEntity)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Index để tìm kiếm theo email và trạng thái
CREATE INDEX idx_contact_messages_email ON contact_messages(sender_email);
CREATE INDEX idx_contact_messages_is_read ON contact_messages(is_read) WHERE is_deleted = FALSE;
CREATE INDEX idx_contact_messages_created_at ON contact_messages(created_at DESC);

-- Comment mô tả bảng
COMMENT ON TABLE contact_messages IS 'Bảng lưu trữ tin nhắn liên hệ từ nhà tuyển dụng/khách truy cập';
COMMENT ON COLUMN contact_messages.sender_name IS 'Tên người gửi';
COMMENT ON COLUMN contact_messages.sender_email IS 'Email người gửi để phản hồi';
COMMENT ON COLUMN contact_messages.subject IS 'Tiêu đề tin nhắn';
COMMENT ON COLUMN contact_messages.message IS 'Nội dung tin nhắn';
COMMENT ON COLUMN contact_messages.is_read IS 'Đã đọc hay chưa';
COMMENT ON COLUMN contact_messages.email_sent IS 'Email thông báo đã gửi thành công hay chưa';
COMMENT ON COLUMN contact_messages.sender_ip IS 'IP của người gửi (tracking spam)';

