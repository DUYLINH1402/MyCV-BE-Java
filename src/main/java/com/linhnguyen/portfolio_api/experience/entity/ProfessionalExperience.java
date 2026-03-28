package com.linhnguyen.portfolio_api.experience.entity;

import com.linhnguyen.portfolio_api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity đại diện cho kinh nghiệm làm việc chuyên nghiệp trong Portfolio.
 */
@Entity
@Table(name = "professional_experiences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalExperience extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "company_logo", length = 500)
    private String companyLogo;

    @Column(name = "location")
    private String location;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description", columnDefinition = "jsonb")
    @Builder.Default
    private List<String> description = List.of();
}

