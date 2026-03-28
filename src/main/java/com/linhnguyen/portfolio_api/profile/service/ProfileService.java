package com.linhnguyen.portfolio_api.profile.service;

import com.linhnguyen.portfolio_api.profile.dto.ProfileUpdateDTO;
import com.linhnguyen.portfolio_api.profile.dto.ProfileResponseDTO;
import com.linhnguyen.portfolio_api.profile.entity.Profile;
import com.linhnguyen.portfolio_api.exception.ResourceNotFoundException;
import com.linhnguyen.portfolio_api.profile.mapper.ProfileMapper;
import com.linhnguyen.portfolio_api.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý logic nghiệp vụ cho Profile.
 * Sử dụng Constructor Injection thông qua @RequiredArgsConstructor.
 * Áp dụng Spring Cache để tối ưu hiệu năng đọc dữ liệu.
 *
 * Lưu ý: Portfolio chỉ có DUY NHẤT 1 profile (chủ sở hữu).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

    private static final String OWNER_PROFILE_CACHE_KEY = "'owner'";

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Cacheable(value = "profile", key = OWNER_PROFILE_CACHE_KEY, unless = "#result == null")
    public ProfileResponseDTO getOwnerProfile() {
        Profile profile = profileRepository.findFirstByIsDeletedFalse()
                .orElseThrow(() -> new ResourceNotFoundException("Profile chưa được khởi tạo"));
        return profileMapper.toResponseDTO(profile);
    }

    @CacheEvict(value = "profile", key = OWNER_PROFILE_CACHE_KEY)
    @Transactional
    public ProfileResponseDTO updateOwnerProfile(ProfileUpdateDTO request) {
        log.info("Đang cập nhật profile owner. Xóa cache.");

        Profile profile = profileRepository.findFirstByIsDeletedFalse()
                .orElseThrow(() -> new ResourceNotFoundException("Profile chưa được khởi tạo"));

        profileMapper.updateEntityFromDTO(request, profile);
        Profile savedProfile = profileRepository.save(profile);

        log.info("Cập nhật profile owner thành công với ID: {}", savedProfile.getId());
        return profileMapper.toResponseDTO(savedProfile);
    }

    @Cacheable(value = "profile", key = "#id", unless = "#result == null")
    public ProfileResponseDTO getProfileById(Long id) {
        log.info("Đang lấy profile từ database với ID: {}", id);
        Profile profile = profileRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        return profileMapper.toResponseDTO(profile);
    }

    public List<ProfileResponseDTO> getAllProfiles() {
        log.info("Đang lấy danh sách tất cả profile từ database");
        return profileRepository.findAllByIsDeletedFalse()
                .stream()
                .map(profileMapper::toResponseDTO)
                .toList();
    }

    @CacheEvict(value = "profile", key = "#id")
    @Transactional
    public ProfileResponseDTO updateProfile(Long id, ProfileUpdateDTO request) {
        log.info("Đang cập nhật profile với ID: {}. Xóa cache.", id);

        Profile profile = profileRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", id));

        profileMapper.updateEntityFromDTO(request, profile);
        Profile savedProfile = profileRepository.save(profile);

        log.info("Cập nhật profile thành công với ID: {}", savedProfile.getId());
        return profileMapper.toResponseDTO(savedProfile);
    }

    @CacheEvict(value = "profile", key = "#id")
    @Transactional
    public void deleteProfile(Long id) {
        log.info("Đang xóa profile với ID: {}. Xóa cache.", id);

        Profile profile = profileRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", id));

        profile.setIsDeleted(true);
        profileRepository.save(profile);

        log.info("Xóa mềm profile thành công với ID: {}", id);
    }

    @CacheEvict(value = "profile", allEntries = true)
    public void refreshProfileCache() {
        log.info("Đã xóa toàn bộ cache profile");
    }
}

