package com.linhnguyen.portfolio_api.entity;

/**
 * Enum định nghĩa các danh mục phân loại kỹ năng.
 * Dùng để nhóm các kỹ năng theo lĩnh vực chuyên môn.
 */
public enum SkillCategory {
    /** Các công nghệ phía client (React, Vue, Angular, HTML, CSS, JavaScript) */
    FRONTEND,

    /** Các công nghệ phía server (Java, Spring Boot, Node.js, Python) */
    BACKEND,

    /** Các hệ quản trị cơ sở dữ liệu (MySQL, PostgreSQL, MongoDB, Redis) */
    DATABASE,

    /** Các công cụ hỗ trợ phát triển (Git, Maven, Gradle, IDE) */
    TOOLS,

    /** Các công cụ DevOps và Cloud (Docker, Kubernetes, AWS, CI/CD) */
    DEVOPS,

    /** Các kỹ năng khác không thuộc các danh mục trên */
    OTHER
}
