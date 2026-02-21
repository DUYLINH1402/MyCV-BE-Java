package com.linhnguyen.portfolio_api.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity đại diện cho các kỹ năng kỹ thuật.
 * Lưu trữ thông tin về tên kỹ năng, danh mục phân loại và mức độ thành thạo.
 */
@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tên của kỹ năng (ví dụ: Java, Spring Boot, React) */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /** Danh mục phân loại kỹ năng (Frontend, Backend, Database, Tools, DevOps) */
    @Column(name = "category", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    /** Mức độ thành thạo (LOW, MEDIUM, HIGH) */
    @Column(name = "level", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SkillLevel level;

    /** Thứ tự ưu tiên hiển thị, giá trị nhỏ sẽ hiển thị trước */
    @Column(name = "priority")
    private Integer priority;
}
