package com.linhnguyen.portfolio_api.experience.repository;

import com.linhnguyen.portfolio_api.experience.entity.ProfessionalExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository xử lý các thao tác với bảng professional_experiences trong database.
 */
@Repository
public interface ProfessionalExperienceRepository extends JpaRepository<ProfessionalExperience, Long> {

    List<ProfessionalExperience> findAllByIsDeletedFalseOrderByStartDateDesc();

    Optional<ProfessionalExperience> findByIdAndIsDeletedFalse(Long id);

    boolean existsByJobTitleAndCompanyAndIsDeletedFalse(String jobTitle, String company);

    boolean existsByJobTitleAndCompanyAndIdNotAndIsDeletedFalse(String jobTitle, String company, Long id);
}

