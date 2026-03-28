package com.linhnguyen.portfolio_api.profile.repository;

import com.linhnguyen.portfolio_api.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository xử lý các thao tác với bảng profile trong database.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByEmailAndIsDeletedFalse(String email);

    List<Profile> findAllByIsDeletedFalse();

    Optional<Profile> findByIdAndIsDeletedFalse(Long id);

    Optional<Profile> findFirstByIsDeletedFalse();
}

