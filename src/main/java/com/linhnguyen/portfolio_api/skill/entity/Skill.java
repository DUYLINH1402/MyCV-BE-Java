package com.linhnguyen.portfolio_api.skill.entity;

import com.linhnguyen.portfolio_api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity đại diện cho các kỹ năng kỹ thuật.
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

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "category", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    @Column(name = "level", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SkillLevel level;

    @Column(name = "priority")
    private Integer priority;
}

