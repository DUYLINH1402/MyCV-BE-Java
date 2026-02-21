package com.linhnguyen.portfolio_api.service;

import com.linhnguyen.portfolio_api.dto.request.ProjectCreateDTO;
import com.linhnguyen.portfolio_api.dto.request.ProjectUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.ProjectResponseDTO;
import com.linhnguyen.portfolio_api.entity.Project;
import com.linhnguyen.portfolio_api.exception.DuplicateResourceException;
import com.linhnguyen.portfolio_api.exception.ResourceNotFoundException;
import com.linhnguyen.portfolio_api.mapper.ProjectMapper;
import com.linhnguyen.portfolio_api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý logic nghiệp vụ cho Project.
 * Sử dụng Constructor Injection thông qua @RequiredArgsConstructor.
 * Áp dụng Spring Cache để tối ưu hiệu năng đọc dữ liệu.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    /**
     * Lấy thông tin project theo ID với caching.
     * Dữ liệu sẽ được cache với key format: projects::1
     * Log chỉ xuất hiện khi cache miss (phải query database).
     *
     * @param id ID của project cần lấy
     * @return DTO chứa thông tin project
     * @throws ResourceNotFoundException nếu không tìm thấy project
     */
    @Cacheable(value = "projects", key = "#id", unless = "#result == null")
    public ProjectResponseDTO getProjectById(Long id) {
        log.info("Đang lấy project từ database với ID: {}", id);
        Project project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        return projectMapper.toResponseDTO(project);
    }

    /**
     * Lấy danh sách tất cả project đang hoạt động.
     * Sắp xếp theo displayOrder tăng dần (project có displayOrder nhỏ hơn hiển thị trước).
     *
     * @return Danh sách DTO chứa thông tin các project
     */
    @Cacheable(value = "projects", key = "'all'")
    public List<ProjectResponseDTO> getAllProjects() {
        log.info("Đang lấy danh sách tất cả project từ database");
        return projectRepository.findAllByIsDeletedFalseOrderByDisplayOrderAsc()
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    /**
     * Lấy danh sách các project nổi bật (isFeatured = true).
     * Sắp xếp theo displayOrder tăng dần.
     *
     * @return Danh sách DTO chứa thông tin các project nổi bật
     */
    @Cacheable(value = "projects", key = "'featured'")
    public List<ProjectResponseDTO> getFeaturedProjects() {
        log.info("Đang lấy danh sách project nổi bật từ database");
        return projectRepository.findAllByIsFeaturedTrueAndIsDeletedFalseOrderByDisplayOrderAsc()
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    /**
     * Lấy danh sách project theo category.
     * Sắp xếp theo displayOrder tăng dần.
     *
     * @param category Phân loại dự án (Web, Mobile, Backend, Fullstack)
     * @return Danh sách DTO chứa thông tin các project theo category
     */
    @Cacheable(value = "projects", key = "'category_' + #category")
    public List<ProjectResponseDTO> getProjectsByCategory(String category) {
        log.info("Đang lấy danh sách project theo category: {}", category);
        return projectRepository.findAllByCategoryAndIsDeletedFalseOrderByDisplayOrderAsc(category)
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    /**
     * Lấy danh sách project theo status.
     * Sắp xếp theo displayOrder tăng dần.
     *
     * @param status Trạng thái dự án (completed, in_progress, archived)
     * @return Danh sách DTO chứa thông tin các project theo status
     */
    @Cacheable(value = "projects", key = "'status_' + #status")
    public List<ProjectResponseDTO> getProjectsByStatus(String status) {
        log.info("Đang lấy danh sách project theo status: {}", status);
        return projectRepository.findAllByStatusAndIsDeletedFalseOrderByDisplayOrderAsc(status)
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    /**
     * Tạo mới project.
     * Kiểm tra trùng lặp tiêu đề trước khi tạo.
     * Sử dụng @Transactional để đảm bảo tính toàn vẹn dữ liệu.
     *
     * @param request DTO chứa thông tin project cần tạo
     * @return DTO chứa thông tin project vừa được tạo
     * @throws DuplicateResourceException nếu tiêu đề đã tồn tại
     */
    @CacheEvict(value = "projects", allEntries = true)
    @Transactional
    public ProjectResponseDTO createProject(ProjectCreateDTO request) {
        log.info("Đang tạo project mới với tiêu đề: {}", request.getTitle());

        // Kiểm tra tiêu đề đã tồn tại chưa
        if (projectRepository.existsByTitleAndIsDeletedFalse(request.getTitle())) {
            throw new DuplicateResourceException("Project", "title", request.getTitle());
        }

        Project project = projectMapper.toEntity(request);
        project.setIsDeleted(false);

        // Thiết lập giá trị mặc định nếu chưa có
        if (project.getIsFeatured() == null) {
            project.setIsFeatured(false);
        }
        if (project.getDisplayOrder() == null) {
            project.setDisplayOrder(0);
        }
        if (project.getStatus() == null) {
            project.setStatus("completed");
        }

        Project savedProject = projectRepository.save(project);

        log.info("Tạo project thành công với ID: {}", savedProject.getId());
        return projectMapper.toResponseDTO(savedProject);
    }

    /**
     * Cập nhật thông tin project.
     * Xóa cache sau khi cập nhật để đảm bảo tính nhất quán dữ liệu.
     *
     * @param id      ID của project cần cập nhật
     * @param request DTO chứa thông tin cập nhật
     * @return DTO chứa thông tin project sau khi cập nhật
     * @throws ResourceNotFoundException nếu không tìm thấy project
     */
    @CacheEvict(value = "projects", allEntries = true)
    @Transactional
    public ProjectResponseDTO updateProject(Long id, ProjectUpdateDTO request) {
        log.info("Đang cập nhật project với ID: {}. Xóa cache.", id);

        Project project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));

        // Kiểm tra tiêu đề trùng lặp (nếu có thay đổi)
        if (request.getTitle() != null && !request.getTitle().equals(project.getTitle())) {
            if (projectRepository.existsByTitleAndIdNotAndIsDeletedFalse(request.getTitle(), id)) {
                throw new DuplicateResourceException("Project", "title", request.getTitle());
            }
        }

        projectMapper.updateEntityFromDTO(request, project);
        Project savedProject = projectRepository.save(project);

        log.info("Cập nhật project thành công với ID: {}", savedProject.getId());
        return projectMapper.toResponseDTO(savedProject);
    }

    /**
     * Xóa mềm project (soft delete).
     * Chỉ đánh dấu isDeleted = true, không xóa vật lý khỏi database.
     * Xóa cache sau khi xóa để đảm bảo tính nhất quán.
     *
     * @param id ID của project cần xóa
     * @throws ResourceNotFoundException nếu không tìm thấy project
     */
    @CacheEvict(value = "projects", allEntries = true)
    @Transactional
    public void deleteProject(Long id) {
        log.info("Đang xóa project với ID: {}. Xóa cache.", id);

        Project project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));

        project.setIsDeleted(true);
        projectRepository.save(project);

        log.info("Xóa mềm project thành công với ID: {}", id);
    }

    /**
     * Xóa toàn bộ cache của projects.
     * Dùng khi cần refresh tất cả dữ liệu cache.
     */
    @CacheEvict(value = "projects", allEntries = true)
    public void refreshAllProjectsCache() {
        log.info("Đang xóa toàn bộ cache của projects");
    }
}
