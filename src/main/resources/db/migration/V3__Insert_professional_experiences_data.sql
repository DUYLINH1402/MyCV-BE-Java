-- =====================================================
-- V3__Insert_professional_experiences_data.sql
-- Thêm dữ liệu kinh nghiệm làm việc chuyên nghiệp
-- =====================================================

INSERT INTO professional_experiences (job_title, company, company_logo, location, start_date, end_date, description, created_at, created_by, is_deleted)
VALUES
(
    'Technical Staff',
    'ITC Mekong',
    'https://res.cloudinary.com/ddia5yfia/image/upload/v1774094981/ITC-mekong-logo_twexxi.jpg',
    'Vietnam',
    '2017-01-01',
    '2019-12-31',
    '[
        "Conducted field measurements of BTS signal coverage for telecom networks (Vinaphone, Mobifone, Vietnamobile)",
        "Collected and analyzed signal data to evaluate network coverage and quality",
        "Prepared technical reports and delivered measurement results to telecom operators",
        "Ensured data accuracy and compliance with measurement standards"
    ]'::jsonb,
    CURRENT_TIMESTAMP,
    'SYSTEM',
    FALSE
),
(
    'Robot Operator',
    'ElringKlinger',
    'https://res.cloudinary.com/ddia5yfia/image/upload/v1774094981/elringklinger-logo_nwccjb.png',
    'Japan',
    '2020-01-01',
    '2022-12-31',
    '[
        "Operated and monitored industrial robots in production environment",
        "Ensured stable system operation and handled basic troubleshooting",
        "Followed strict operational procedures and improved work efficiency",
        "Developed discipline, attention to detail, and problem-solving skills"
    ]'::jsonb,
    CURRENT_TIMESTAMP,
    'SYSTEM',
    FALSE
),
(
    'Production Operator',
    'SHOEI PRINT MFG CO.LTD',
    'https://res.cloudinary.com/ddia5yfia/image/upload/v1774098790/ShoeiPrint-logo_ai6jet.png',
    'Japan',
    '2022-01-01',
    NULL,
    '[
        "Operated and supported PCB (Printed Circuit Board) manufacturing processes",
        "Followed strict production procedures to ensure product quality",
        "Performed basic inspection and quality checks on electronic components",
        "Maintained discipline, attention to detail, and efficiency in a high-standard working environment"
    ]'::jsonb,
    CURRENT_TIMESTAMP,
    'SYSTEM',
    FALSE
);

