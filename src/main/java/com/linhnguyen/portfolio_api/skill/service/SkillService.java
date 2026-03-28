package com.linhnguyen.portfolio_api.skill.service;

import com.linhnguyen.portfolio_api.skill.dto.SkillCreateDTO;
import com.linhnguyen.portfolio_api.skill.dto.SkillUpdateDTO;
import com.linhnguyen.portfolio_api.skill.dto.SkillResponseDTO;
import com.linhnguyen.portfolio_api.skill.entity.Skill;
import com.linhnguyen.portfolio_api.skill.entity.SkillCategory;
import com.linhnguyen.portfolio_api.exception.DuplicateResourceException;
import com.linhnguyen.portfolio_api.exception.ResourceNotFoundException;
import com.linhnguyen.portfolio_api.skill.mapper.SkillMapper;
import com.linhnguyen.portfolio_api.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý logic nghiệp vụ cho Skill.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    @Cacheable(value = "skills", key = "#id", unless = "#result == null")
    public SkillResponseDTO getSkillById(Long id) {
        log.info("Đang lấy skill từ database với ID: {}", id);
        Skill skill = skillRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", id));
        return skillMapper.toResponseDTO(skill);
    }

    public List<SkillResponseDTO> getAllSkills() {
        return skillRepository.findAllByIsDeletedFalse()
                .stream()
                .map(skillMapper::toResponseDTO)
                .toList();
    }

    public List<SkillResponseDTO> getSkillsByCategory(SkillCategory category) {
        log.info("Đang lấy danh sách skill theo danh mục: {}", category);
        return skillRepository.findByCategoryAndIsDeletedFalseOrderByPriorityAsc(category)
                .stream()
                .map(skillMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public SkillResponseDTO createSkill(SkillCreateDTO request) {
        log.info("Đang tạo skill mới với tên: {}", request.getName());

        if (skillRepository.existsByNameAndIsDeletedFalse(request.getName())) {
            throw new DuplicateResourceException("Skill", "name", request.getName());
        }

        Skill skill = skillMapper.toEntity(request);
        skill.setIsDeleted(false);
        Skill savedSkill = skillRepository.save(skill);

        log.info("Tạo skill thành công với ID: {}", savedSkill.getId());
        return skillMapper.toResponseDTO(savedSkill);
    }

    @CacheEvict(value = "skills", key = "#id")
    @Transactional
    public SkillResponseDTO updateSkill(Long id, SkillUpdateDTO request) {
        log.info("Đang cập nhật skill với ID: {}. Xóa cache.", id);

        Skill skill = skillRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", id));

        skillMapper.updateEntityFromDTO(request, skill);
        Skill savedSkill = skillRepository.save(skill);

        log.info("Cập nhật skill thành công với ID: {}", savedSkill.getId());
        return skillMapper.toResponseDTO(savedSkill);
    }

    @CacheEvict(value = "skills", key = "#id")
    @Transactional
    public void deleteSkill(Long id) {
        log.info("Đang xóa skill với ID: {}. Xóa cache.", id);

        Skill skill = skillRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", id));

        skill.setIsDeleted(true);
        skillRepository.save(skill);

        log.info("Xóa mềm skill thành công với ID: {}", id);
    }

    @CacheEvict(value = "skills", allEntries = true)
    public void refreshAllSkillsCache() {
        log.info("Đang xóa toàn bộ cache của skills");
    }
}

