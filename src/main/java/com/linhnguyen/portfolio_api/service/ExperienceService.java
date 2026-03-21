package com.linhnguyen.portfolio_api.service;

import com.linhnguyen.portfolio_api.dto.request.ExperienceCreateDTO;
import com.linhnguyen.portfolio_api.dto.request.ExperienceUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.ExperienceResponseDTO;
import com.linhnguyen.portfolio_api.entity.ProfessionalExperience;
import com.linhnguyen.portfolio_api.exception.DuplicateResourceException;
import com.linhnguyen.portfolio_api.exception.ResourceNotFoundException;
import com.linhnguyen.portfolio_api.mapper.ExperienceMapper;
import com.linhnguyen.portfolio_api.repository.ProfessionalExperienceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý logic nghiệp vụ cho Professional Experience.
 * Sử dụng Constructor Injection thông qua @RequiredArgsConstructor.
 * Áp dụng Spring Cache để tối ưu hiệu năng đọc dữ liệu.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExperienceService {

    private final ProfessionalExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;

    /**
     * Lấy danh sách tất cả kinh nghiệm đang hoạt động.
     * Sắp xếp theo startDate giảm dần (kinh nghiệm gần nhất hiển thị trước).
     *
     * @return Danh sách DTO chứa thông tin các experience
     */
    @Cacheable(value = "experiences", key = "'all'")
    public List<ExperienceResponseDTO> getAllExperiences() {
        return experienceRepository.findAllByIsDeletedFalseOrderByStartDateDesc()
                .stream()
                .map(experienceMapper::toResponseDTO)
                .toList();
    }

    /**
     * Lấy thông tin experience theo ID với caching.
     * Dữ liệu sẽ được cache với key format: experiences::1
     *
     * @param id ID của experience cần lấy
     * @return DTO chứa thông tin experience
     * @throws ResourceNotFoundException nếu không tìm thấy experience
     */
    @Cacheable(value = "experiences", key = "#id", unless = "#result == null")
    public ExperienceResponseDTO getExperienceById(Long id) {
        log.info("Đang lấy experience từ database với ID: {}", id);
        ProfessionalExperience experience = experienceRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        return experienceMapper.toResponseDTO(experience);
    }

    /**
     * Tạo mới experience.
     * Kiểm tra trùng lặp cặp (jobTitle, company) trước khi tạo.
     * Sử dụng @Transactional để đảm bảo tính toàn vẹn dữ liệu.
     *
     * @param request DTO chứa thông tin experience cần tạo
     * @return DTO chứa thông tin experience vừa được tạo
     * @throws DuplicateResourceException nếu cặp (jobTitle, company) đã tồn tại
     */
    @CacheEvict(value = "experiences", allEntries = true)
    @Transactional
    public ExperienceResponseDTO createExperience(ExperienceCreateDTO request) {
        log.info("Đang tạo experience mới: {} tại {}", request.getJobTitle(), request.getCompany());

        // Kiểm tra trùng lặp cặp (jobTitle, company)
        if (experienceRepository.existsByJobTitleAndCompanyAndIsDeletedFalse(
                request.getJobTitle(), request.getCompany())) {
            throw new DuplicateResourceException("Experience", "jobTitle + company",
                    request.getJobTitle() + " at " + request.getCompany());
        }

        ProfessionalExperience experience = experienceMapper.toEntity(request);
        experience.setIsDeleted(false);

        // Thiết lập giá trị mặc định cho description nếu null
        if (experience.getDescription() == null) {
            experience.setDescription(List.of());
        }

        ProfessionalExperience saved = experienceRepository.save(experience);

        log.info("Tạo experience thành công với ID: {}", saved.getId());
        return experienceMapper.toResponseDTO(saved);
    }

    /**
     * Cập nhật thông tin experience.
     * Xóa cache sau khi cập nhật để đảm bảo tính nhất quán dữ liệu.
     *
     * @param id      ID của experience cần cập nhật
     * @param request DTO chứa thông tin cập nhật
     * @return DTO chứa thông tin experience sau khi cập nhật
     * @throws ResourceNotFoundException  nếu không tìm thấy experience
     * @throws DuplicateResourceException nếu cặp (jobTitle, company) đã tồn tại ở record khác
     */
    @CacheEvict(value = "experiences", allEntries = true)
    @Transactional
    public ExperienceResponseDTO updateExperience(Long id, ExperienceUpdateDTO request) {
        log.info("Đang cập nhật experience với ID: {}. Xóa cache.", id);

        ProfessionalExperience experience = experienceRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));

        // Kiểm tra trùng lặp cặp (jobTitle, company) nếu có thay đổi
        String newJobTitle = request.getJobTitle() != null ? request.getJobTitle() : experience.getJobTitle();
        String newCompany = request.getCompany() != null ? request.getCompany() : experience.getCompany();

        if (!newJobTitle.equals(experience.getJobTitle()) || !newCompany.equals(experience.getCompany())) {
            if (experienceRepository.existsByJobTitleAndCompanyAndIdNotAndIsDeletedFalse(
                    newJobTitle, newCompany, id)) {
                throw new DuplicateResourceException("Experience", "jobTitle + company",
                        newJobTitle + " at " + newCompany);
            }
        }

        experienceMapper.updateEntityFromDTO(request, experience);
        ProfessionalExperience saved = experienceRepository.save(experience);

        log.info("Cập nhật experience thành công với ID: {}", saved.getId());
        return experienceMapper.toResponseDTO(saved);
    }

    /**
     * Xóa mềm experience (soft delete).
     * Chỉ đánh dấu isDeleted = true, không xóa vật lý khỏi database.
     *
     * @param id ID của experience cần xóa
     * @throws ResourceNotFoundException nếu không tìm thấy experience
     */
    @CacheEvict(value = "experiences", allEntries = true)
    @Transactional
    public void deleteExperience(Long id) {
        log.info("Đang xóa experience với ID: {}. Xóa cache.", id);

        ProfessionalExperience experience = experienceRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));

        experience.setIsDeleted(true);
        experienceRepository.save(experience);

        log.info("Xóa mềm experience thành công với ID: {}", id);
    }

    /**
     * Xóa toàn bộ cache của experiences.
     * Dùng khi cần refresh tất cả dữ liệu cache.
     */
    @CacheEvict(value = "experiences", allEntries = true)
    public void refreshAllExperiencesCache() {
        log.info("Đang xóa toàn bộ cache của experiences");
    }
}

