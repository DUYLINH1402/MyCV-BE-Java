package com.linhnguyen.portfolio_api.service;

import com.linhnguyen.portfolio_api.dto.request.ProfileUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.ProfileResponseDTO;
import com.linhnguyen.portfolio_api.entity.Profile;
import com.linhnguyen.portfolio_api.exception.ResourceNotFoundException;
import com.linhnguyen.portfolio_api.mapper.ProfileMapper;
import com.linhnguyen.portfolio_api.repository.ProfileRepository;
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

    /** Cache key cố định cho owner profile vì chỉ có 1 profile duy nhất */
    private static final String OWNER_PROFILE_CACHE_KEY = "'owner'";

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    /**
     * Lấy thông tin profile của chủ sở hữu Portfolio.
     * Portfolio chỉ có 1 profile duy nhất nên lấy record đầu tiên.
     * Sử dụng cache với key cố định 'owner'.
     *
     * @return DTO chứa thông tin profile
     * @throws ResourceNotFoundException nếu chưa có profile nào
     */
    @Cacheable(value = "profile", key = OWNER_PROFILE_CACHE_KEY, unless = "#result == null")
    public ProfileResponseDTO getOwnerProfile() {
        log.info("Đang lấy thông tin profile owner từ database");
        Profile profile = profileRepository.findFirstByIsDeletedFalse()
                .orElseThrow(() -> new ResourceNotFoundException("Profile chưa được khởi tạo"));
        return profileMapper.toResponseDTO(profile);
    }

    /**
     * Cập nhật thông tin profile của chủ sở hữu Portfolio.
     * Portfolio chỉ có 1 profile duy nhất nên không cần truyền ID.
     * Xóa cache sau khi cập nhật để đảm bảo tính nhất quán.
     *
     * @param request DTO chứa thông tin cập nhật
     * @return DTO chứa thông tin profile sau khi cập nhật
     * @throws ResourceNotFoundException nếu chưa có profile nào
     */
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

    /**
     * Lấy thông tin profile theo ID với caching.
     * Dữ liệu sẽ được cache với key format: profile::1
     * Log chỉ xuất hiện khi cache miss (phải query database).
     *
     * @param id ID của profile cần lấy
     * @return DTO chứa thông tin profile
     * @throws ResourceNotFoundException nếu không tìm thấy profile
     */
    @Cacheable(value = "profile", key = "#id", unless = "#result == null")
    public ProfileResponseDTO getProfileById(Long id) {
        log.info("Đang lấy profile từ database với ID: {}", id);
        Profile profile = profileRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        return profileMapper.toResponseDTO(profile);
    }

    /**
     * Lấy danh sách tất cả profile đang hoạt động.
     *
     * @return Danh sách DTO chứa thông tin các profile
     */
    public List<ProfileResponseDTO> getAllProfiles() {
        log.info("Đang lấy danh sách tất cả profile từ database");
        return profileRepository.findAllByIsDeletedFalse()
                .stream()
                .map(profileMapper::toResponseDTO)
                .toList();
    }


    /**
     * Cập nhật thông tin profile.
     * Xóa cache sau khi cập nhật để đảm bảo tính nhất quán dữ liệu.
     *
     * @param id      ID của profile cần cập nhật
     * @param request DTO chứa thông tin cập nhật
     * @return DTO chứa thông tin profile sau khi cập nhật
     * @throws ResourceNotFoundException nếu không tìm thấy profile
     */
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

    /**
     * Xóa mềm profile (soft delete).
     * Chỉ đánh dấu isDeleted = true, không xóa vật lý khỏi database.
     * Xóa cache sau khi xóa để đảm bảo tính nhất quán.
     *
     * @param id ID của profile cần xóa
     * @throws ResourceNotFoundException nếu không tìm thấy profile
     */
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

    /**
     * Xóa toàn bộ cache của profile.
     * Sử dụng khi cần làm mới dữ liệu cache sau khi thay đổi schema hoặc migration.
     */
    @CacheEvict(value = "profile", allEntries = true)
    public void refreshProfileCache() {
        log.info("Đã xóa toàn bộ cache profile");
    }
}

