package com.linhnguyen.portfolio_api.project.service;

import com.linhnguyen.portfolio_api.project.dto.ProjectCreateDTO;
import com.linhnguyen.portfolio_api.project.dto.ProjectUpdateDTO;
import com.linhnguyen.portfolio_api.project.dto.ProjectResponseDTO;
import com.linhnguyen.portfolio_api.project.entity.Project;
import com.linhnguyen.portfolio_api.exception.DuplicateResourceException;
import com.linhnguyen.portfolio_api.exception.ResourceNotFoundException;
import com.linhnguyen.portfolio_api.project.mapper.ProjectMapper;
import com.linhnguyen.portfolio_api.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service xử lý logic nghiệp vụ cho Project.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Cacheable(value = "projects", key = "#id", unless = "#result == null")
    public ProjectResponseDTO getProjectById(Long id) {
        log.info("Đang lấy project từ database với ID: {}", id);
        Project project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
        return projectMapper.toResponseDTO(project);
    }

    @Cacheable(value = "projects", key = "'all'")
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAllByIsDeletedFalseOrderByDisplayOrderAsc()
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    @Cacheable(value = "projects", key = "'featured'")
    public List<ProjectResponseDTO> getFeaturedProjects() {
        log.info("Đang lấy danh sách project nổi bật từ database");
        return projectRepository.findAllByIsFeaturedTrueAndIsDeletedFalseOrderByDisplayOrderAsc()
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    @Cacheable(value = "projects", key = "'category_' + #category")
    public List<ProjectResponseDTO> getProjectsByCategory(String category) {
        log.info("Đang lấy danh sách project theo category: {}", category);
        return projectRepository.findAllByCategoryAndIsDeletedFalseOrderByDisplayOrderAsc(category)
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    @Cacheable(value = "projects", key = "'status_' + #status")
    public List<ProjectResponseDTO> getProjectsByStatus(String status) {
        log.info("Đang lấy danh sách project theo status: {}", status);
        return projectRepository.findAllByStatusAndIsDeletedFalseOrderByDisplayOrderAsc(status)
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    @CacheEvict(value = "projects", allEntries = true)
    @Transactional
    public ProjectResponseDTO createProject(ProjectCreateDTO request) {
        log.info("Đang tạo project mới với tiêu đề: {}", request.getTitle());

        if (projectRepository.existsByTitleAndIsDeletedFalse(request.getTitle())) {
            throw new DuplicateResourceException("Project", "title", request.getTitle());
        }

        Project project = projectMapper.toEntity(request);
        project.setIsDeleted(false);

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

    @CacheEvict(value = "projects", allEntries = true)
    @Transactional
    public ProjectResponseDTO updateProject(Long id, ProjectUpdateDTO request) {
        log.info("Đang cập nhật project với ID: {}. Xóa cache.", id);

        Project project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));

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

    @CacheEvict(value = "projects", allEntries = true)
    public void refreshAllProjectsCache() {
        log.info("Đang xóa toàn bộ cache của projects");
    }
}

