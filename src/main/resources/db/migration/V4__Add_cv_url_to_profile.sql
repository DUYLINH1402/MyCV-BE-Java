-- Thêm cột cv_url vào bảng profile để lưu link download CV bản cứng (PDF)
ALTER TABLE profile ADD COLUMN IF NOT EXISTS cv_url VARCHAR(500);

