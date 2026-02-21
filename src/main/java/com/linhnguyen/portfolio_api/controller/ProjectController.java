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
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Projects", description = "API công khai xem dự án")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Lấy danh sách tất cả project.
     * Sắp xếp theo displayOrder tăng dần.
     *
     * @return Danh sách project đang hoạt động
     */
    @GetMapping
    @Operation(summary = "Lấy tất cả projects", description = "Lấy danh sách tất cả project, sắp xếp theo displayOrder. Không yêu cầu xác thực.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getAllProjects() {
        log.info("Request lấy danh sách tất cả projects");
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
    @Operation(summary = "Lấy projects nổi bật", description = "Lấy danh sách project được đánh dấu nổi bật (isFeatured = true)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getFeaturedProjects() {
        log.info("Request lấy danh sách project nổi bật");
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
    @Operation(summary = "Lấy projects theo category", description = "Lấy danh sách project theo phân loại")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getProjectsByCategory(
            @Parameter(description = "Phân loại dự án", example = "Backend", required = true)
            @PathVariable String category) {
        log.info("Request lấy danh sách project theo category: {}", category);
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
    @Operation(summary = "Lấy projects theo status", description = "Lấy danh sách project theo trạng thái")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    public ResponseEntity<ApiResponse<List<ProjectResponseDTO>>> getProjectsByStatus(
            @Parameter(description = "Trạng thái dự án", example = "completed", required = true)
            @PathVariable String status) {
        log.info("Request lấy danh sách project theo status: {}", status);
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
    @Operation(summary = "Lấy project theo ID", description = "Lấy thông tin chi tiết của project theo ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy project thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy project")
    })
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> getProjectById(
            @Parameter(description = "ID của project", example = "1", required = true)
            @PathVariable Long id) {
        log.info("Request lấy project với ID: {}", id);
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(project));
    }
}
