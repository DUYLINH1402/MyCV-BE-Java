-- =====================================================
-- V2__Create_professional_experiences_table.sql
-- Tạo bảng lưu trữ kinh nghiệm làm việc chuyên nghiệp
-- =====================================================

CREATE TABLE IF NOT EXISTS professional_experiences (
    id BIGSERIAL PRIMARY KEY,

    -- Thông tin công việc
    job_title VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    company_logo VARCHAR(500),
    location VARCHAR(255),
    start_date DATE NOT NULL,
    end_date DATE,
    description JSONB DEFAULT '[]',

    -- Audit fields (từ BaseEntity)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Index để tìm kiếm và sắp xếp
CREATE INDEX idx_prof_exp_company ON professional_experiences(company);
CREATE INDEX idx_prof_exp_start_date ON professional_experiences(start_date DESC);
CREATE INDEX idx_prof_exp_is_deleted ON professional_experiences(is_deleted) WHERE is_deleted = FALSE;

-- Comment mô tả bảng
COMMENT ON TABLE professional_experiences IS 'Bảng lưu trữ kinh nghiệm làm việc chuyên nghiệp của chủ portfolio';
COMMENT ON COLUMN professional_experiences.job_title IS 'Chức danh/vị trí công việc';
COMMENT ON COLUMN professional_experiences.company IS 'Tên công ty/tổ chức';
COMMENT ON COLUMN professional_experiences.company_logo IS 'URL logo công ty (hiển thị trên timeline)';
COMMENT ON COLUMN professional_experiences.location IS 'Địa điểm làm việc (thành phố, quốc gia)';
COMMENT ON COLUMN professional_experiences.start_date IS 'Ngày bắt đầu làm việc';
COMMENT ON COLUMN professional_experiences.end_date IS 'Ngày kết thúc (NULL nếu đang làm việc)';
COMMENT ON COLUMN professional_experiences.description IS 'Mô tả công việc dạng JSON array các bullet points';

