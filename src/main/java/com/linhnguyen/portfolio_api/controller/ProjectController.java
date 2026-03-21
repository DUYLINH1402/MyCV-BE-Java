package com.linhnguyen.portfolio_api.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.response.ProjectResponseDTO;
import com.linhnguyen.portfolio_api.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller xử lý các API công khai liên quan đến Project.
 * Chỉ chứa các endpoint GET để hiển thị thông tin trên Portfolio.
 * Các API quản lý (POST, PUT, DELETE) được tách riêng vào AdminProjectController.
 */
@RestController
@RequestMapping("/v1/public/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Projects", description = "Public API to view portfolio projects")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Lấy danh sách tất cả project.
     * Sắp xếp theo displayOrder tăng dần.
     *
     * @return Danh sách project đang hoạt động
     */
    @GetMapping
    @Operation(summary = "Get All Projects", description = "Retrieve all projects sorted by displayOrder. No authentication required.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Projects retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getAllProjects() {
        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    /**
     * Lấy danh sách các project nổi bật.
     * Chỉ lấy các project có isFeatured = true, sắp xếp theo displayOrder.
     *
     * @return Danh sách project nổi bật
     */
    @GetMapping("/featured")
    @Operation(summary = "Get Featured Projects", description = "Retrieve projects marked as featured (isFeatured = true)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Featured projects retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getFeaturedProjects() {
        log.info("Request to get featured projects");
        List<ProjectResponseDTO> projects = projectService.getFeaturedProjects();
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    /**
     * Lấy danh sách project theo category.
     *
     * @param category Phân loại dự án (Web, Mobile, Backend, Fullstack)
     * @return Danh sách project theo category
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Get Projects by Category", description = "Retrieve projects filtered by category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Projects retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getProjectsByCategory(
            @Parameter(description = "Project category", example = "Backend", required = true)
            @PathVariable String category) {
        log.info("Request to get projects by category: {}", category);
        List<ProjectResponseDTO> projects = projectService.getProjectsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    /**
     * Lấy danh sách project theo status.
     *
     * @param status Trạng thái dự án (completed, in_progress, archived)
     * @return Danh sách project theo status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get Projects by Status", description = "Retrieve projects filtered by status")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Projects retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getProjectsByStatus(
            @Parameter(description = "Project status", example = "completed", required = true)
            @PathVariable String status) {
        log.info("Request to get projects by status: {}", status);
        List<ProjectResponseDTO> projects = projectService.getProjectsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(projects));
    }

    /**
     * Lấy thông tin project theo ID.
     *
     * @param id ID của project cần lấy
     * @return Thông tin project
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get Project by ID", description = "Retrieve detailed project information by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> getProjectById(
            @Parameter(description = "Project ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("Request to get project with ID: {}", id);
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(project));
    }
}
