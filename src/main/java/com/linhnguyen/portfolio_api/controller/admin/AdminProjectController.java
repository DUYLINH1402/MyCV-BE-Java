package com.linhnguyen.portfolio_api.controller.admin;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.request.ProjectCreateDTO;
import com.linhnguyen.portfolio_api.dto.request.ProjectUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.ProjectResponseDTO;
import com.linhnguyen.portfolio_api.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller xử lý các API quản lý Project dành cho Admin.
 * Các endpoint này yêu cầu xác thực và phân quyền Admin.
 * Được tách riêng để dễ dàng cấu hình Spring Security sau này.
 */
@RestController
@RequestMapping("/v1/admin/projects")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin - Projects", description = "Admin project management API (Requires JWT authentication)")
@SecurityRequirement(name = "bearerAuth")
public class AdminProjectController {

    private final ProjectService projectService;

    /**
     * Tạo mới project.
     * Chỉ Admin mới có quyền tạo project mới.
     *
     * @param request DTO chứa thông tin project cần tạo
     * @return Thông tin project vừa tạo với status 201 Created
     */
    @PostMapping
    @Operation(summary = "Create Project", description = "Admin creates a new project")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Project created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token")
    })
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> createProject(
            @Valid @RequestBody ProjectCreateDTO request) {
        log.info("[ADMIN] Request to create new project");
        ProjectResponseDTO project = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(project));
    }

    /**
     * Cập nhật thông tin project.
     * Chỉ Admin mới có quyền cập nhật thông tin project.
     *
     * @param id      ID của project cần cập nhật
     * @param request DTO chứa thông tin cập nhật
     * @return Thông tin project sau khi cập nhật
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update Project", description = "Admin updates project information by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> updateProject(
            @Parameter(description = "Project ID", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateDTO request) {
        log.info("[ADMIN] Request to update project with ID: {}", id);
        ProjectResponseDTO project = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success("Project updated successfully", project));
    }

    /**
     * Xóa project (soft delete).
     * Chỉ Admin mới có quyền xóa project.
     *
     * @param id ID của project cần xóa
     * @return Thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Project", description = "Admin soft-deletes a project by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @Parameter(description = "Project ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("[ADMIN] Request to delete project with ID: {}", id);
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Project deleted successfully"));
    }
}
