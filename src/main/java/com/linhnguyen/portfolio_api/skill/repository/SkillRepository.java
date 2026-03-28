package com.linhnguyen.portfolio_api.skill.repository;

import com.linhnguyen.portfolio_api.skill.entity.Skill;
import com.linhnguyen.portfolio_api.skill.entity.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository xử lý các thao tác với bảng skills trong database.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findAllByIsDeletedFalse();

    Optional<Skill> findByIdAndIsDeletedFalse(Long id);

    List<Skill> findByCategoryAndIsDeletedFalse(SkillCategory category);

    List<Skill> findByCategoryAndIsDeletedFalseOrderByPriorityAsc(SkillCategory category);

    boolean existsByNameAndIsDeletedFalse(String name);
}

