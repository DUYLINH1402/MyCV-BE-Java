package com.linhnguyen.portfolio_api.service;

import com.linhnguyen.portfolio_api.dto.request.SkillCreateDTO;
import com.linhnguyen.portfolio_api.dto.request.SkillUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.SkillResponseDTO;
import com.linhnguyen.portfolio_api.entity.Skill;
import com.linhnguyen.portfolio_api.entity.SkillCategory;
import com.linhnguyen.portfolio_api.exception.DuplicateResourceException;
import com.linhnguyen.portfolio_api.exception.ResourceNotFoundException;
import com.linhnguyen.portfolio_api.mapper.SkillMapper;
import com.linhnguyen.portfolio_api.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý logic nghiệp vụ cho Skill.
 * Sử dụng Constructor Injection thông qua @RequiredArgsConstructor.
 * Áp dụng Spring Cache để tối ưu hiệu năng đọc dữ liệu.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    /**
     * Lấy thông tin skill theo ID với caching.
     * Dữ liệu sẽ được cache với key format: skills::1
     * Log chỉ xuất hiện khi cache miss (phải query database).
     *
     * @param id ID của skill cần lấy
     * @return DTO chứa thông tin skill
     * @throws ResourceNotFoundException nếu không tìm thấy skill
     */
    @Cacheable(value = "skills", key = "#id", unless = "#result == null")
    public SkillResponseDTO getSkillById(Long id) {
        log.info("Đang lấy skill từ database với ID: {}", id);
        Skill skill = skillRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", id));
        return skillMapper.toResponseDTO(skill);
    }

    /**
     * Lấy danh sách tất cả skill đang hoạt động.
     *
     * @return Danh sách DTO chứa thông tin các skill
     */
    public List<SkillResponseDTO> getAllSkills() {
        log.info("Đang lấy danh sách tất cả skill từ database");
        return skillRepository.findAllByIsDeletedFalse()
                .stream()
                .map(skillMapper::toResponseDTO)
                .toList();
    }

    /**
     * Lấy danh sách skill theo danh mục.
     * Ví dụ: Lấy tất cả skill thuộc danh mục BACKEND hoặc FRONTEND.
     * Kết quả được sắp xếp theo priority tăng dần.
     *
     * @param category Danh mục cần lọc
     * @return Danh sách DTO chứa thông tin các skill thuộc danh mục đó
     */
    public List<SkillResponseDTO> getSkillsByCategory(SkillCategory category) {
        log.info("Đang lấy danh sách skill theo danh mục: {}", category);
        return skillRepository.findByCategoryAndIsDeletedFalseOrderByPriorityAsc(category)
                .stream()
                .map(skillMapper::toResponseDTO)
                .toList();
    }

    /**
     * Tạo mới skill.
     * Kiểm tra trùng lặp tên trước khi tạo.
     * Sử dụng @Transactional để đảm bảo tính toàn vẹn dữ liệu.
     *
     * @param request DTO chứa thông tin skill cần tạo
     * @return DTO chứa thông tin skill vừa được tạo
     * @throws DuplicateResourceException nếu tên skill đã tồn tại
     */
    @Transactional
    public SkillResponseDTO createSkill(SkillCreateDTO request) {
        log.info("Đang tạo skill mới với tên: {}", request.getName());

        // Kiểm tra tên skill đã tồn tại chưa
        if (skillRepository.existsByNameAndIsDeletedFalse(request.getName())) {
            throw new DuplicateResourceException("Skill", "name", request.getName());
        }

        Skill skill = skillMapper.toEntity(request);
        skill.setIsDeleted(false);
        Skill savedSkill = skillRepository.save(skill);

        log.info("Tạo skill thành công với ID: {}", savedSkill.getId());
        return skillMapper.toResponseDTO(savedSkill);
    }

    /**
     * Cập nhật thông tin skill.
     * Xóa cache sau khi cập nhật để đảm bảo tính nhất quán dữ liệu.
     *
     * @param id      ID của skill cần cập nhật
     * @param request DTO chứa thông tin cập nhật
     * @return DTO chứa thông tin skill sau khi cập nhật
     * @throws ResourceNotFoundException nếu không tìm thấy skill
     */
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

    /**
     * Xóa mềm skill (soft delete).
     * Chỉ đánh dấu isDeleted = true, không xóa vật lý khỏi database.
     * Xóa cache sau khi xóa để đảm bảo tính nhất quán.
     *
     * @param id ID của skill cần xóa
     * @throws ResourceNotFoundException nếu không tìm thấy skill
     */
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

    /**
     * Xóa toàn bộ cache của skills.
     * Dùng khi cần refresh tất cả dữ liệu cache.
     */
    @CacheEvict(value = "skills", allEntries = true)
    public void refreshAllSkillsCache() {
        log.info("Đang xóa toàn bộ cache của skills");
    }
}
