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
@Tag(name = "Admin - Projects", description = "API quản lý project dành cho Admin (Yêu cầu xác thực)")
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
    @Operation(summary = "Tạo project mới", description = "Admin tạo mới một project")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Tạo project thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ")
    })
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> createProject(
            @Valid @RequestBody ProjectCreateDTO request) {
        log.info("[ADMIN] Request tạo project mới");
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
    @Operation(summary = "Cập nhật project", description = "Admin cập nhật thông tin project theo ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cập nhật project thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy project")
    })
    public ResponseEntity<ApiResponse<ProjectResponseDTO>> updateProject(
            @Parameter(description = "ID của project", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateDTO request) {
        log.info("[ADMIN] Request cập nhật project với ID: {}", id);
        ProjectResponseDTO project = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật project thành công", project));
    }

    /**
     * Xóa project (soft delete).
     * Chỉ Admin mới có quyền xóa project.
     *
     * @param id ID của project cần xóa
     * @return Thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa project", description = "Admin xóa mềm project theo ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Xóa project thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy project")
    })
    public ResponseEntity<ApiResponse<Void>> deleteProject(
            @Parameter(description = "ID của project", example = "1", required = true)
            @PathVariable Long id) {
        log.info("[ADMIN] Request xóa project với ID: {}", id);
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa project thành công", null));
    }
}

