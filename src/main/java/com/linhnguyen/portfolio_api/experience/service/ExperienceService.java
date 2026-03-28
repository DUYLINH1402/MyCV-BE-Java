package com.linhnguyen.portfolio_api.experience.service;

import com.linhnguyen.portfolio_api.experience.dto.ExperienceCreateDTO;
import com.linhnguyen.portfolio_api.experience.dto.ExperienceUpdateDTO;
import com.linhnguyen.portfolio_api.experience.dto.ExperienceResponseDTO;
import com.linhnguyen.portfolio_api.experience.entity.ProfessionalExperience;
import com.linhnguyen.portfolio_api.exception.DuplicateResourceException;
import com.linhnguyen.portfolio_api.exception.ResourceNotFoundException;
import com.linhnguyen.portfolio_api.experience.mapper.ExperienceMapper;
import com.linhnguyen.portfolio_api.experience.repository.ProfessionalExperienceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý logic nghiệp vụ cho Professional Experience.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExperienceService {

    private final ProfessionalExperienceRepository experienceRepository;
    private final ExperienceMapper experienceMapper;

    @Cacheable(value = "experiences", key = "'all'")
    public List<ExperienceResponseDTO> getAllExperiences() {
        return experienceRepository.findAllByIsDeletedFalseOrderByStartDateDesc()
                .stream()
                .map(experienceMapper::toResponseDTO)
                .toList();
    }

    @Cacheable(value = "experiences", key = "#id", unless = "#result == null")
    public ExperienceResponseDTO getExperienceById(Long id) {
        log.info("Đang lấy experience từ database với ID: {}", id);
        ProfessionalExperience experience = experienceRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));
        return experienceMapper.toResponseDTO(experience);
    }

    @CacheEvict(value = "experiences", allEntries = true)
    @Transactional
    public ExperienceResponseDTO createExperience(ExperienceCreateDTO request) {
        log.info("Đang tạo experience mới: {} tại {}", request.getJobTitle(), request.getCompany());

        if (experienceRepository.existsByJobTitleAndCompanyAndIsDeletedFalse(
                request.getJobTitle(), request.getCompany())) {
            throw new DuplicateResourceException("Experience", "jobTitle + company",
                    request.getJobTitle() + " at " + request.getCompany());
        }

        ProfessionalExperience experience = experienceMapper.toEntity(request);
        experience.setIsDeleted(false);

        if (experience.getDescription() == null) {
            experience.setDescription(List.of());
        }

        ProfessionalExperience saved = experienceRepository.save(experience);

        log.info("Tạo experience thành công với ID: {}", saved.getId());
        return experienceMapper.toResponseDTO(saved);
    }

    @CacheEvict(value = "experiences", allEntries = true)
    @Transactional
    public ExperienceResponseDTO updateExperience(Long id, ExperienceUpdateDTO request) {
        log.info("Đang cập nhật experience với ID: {}. Xóa cache.", id);

        ProfessionalExperience experience = experienceRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", id));

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

    @CacheEvict(value = "experiences", allEntries = true)
    public void refreshAllExperiencesCache() {
        log.info("Đang xóa toàn bộ cache của experiences");
    }
}

