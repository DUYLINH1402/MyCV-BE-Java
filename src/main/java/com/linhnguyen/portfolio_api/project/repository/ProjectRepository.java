package com.linhnguyen.portfolio_api.project.repository;

import com.linhnguyen.portfolio_api.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository xử lý các thao tác với bảng projects trong database.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByIsDeletedFalseOrderByDisplayOrderAsc();

    List<Project> findAllByIsFeaturedTrueAndIsDeletedFalseOrderByDisplayOrderAsc();

    List<Project> findAllByCategoryAndIsDeletedFalseOrderByDisplayOrderAsc(String category);

    List<Project> findAllByStatusAndIsDeletedFalseOrderByDisplayOrderAsc(String status);

    Optional<Project> findByIdAndIsDeletedFalse(Long id);

    boolean existsByTitleAndIsDeletedFalse(String title);

    boolean existsByTitleAndIdNotAndIsDeletedFalse(String title, Long id);
}

