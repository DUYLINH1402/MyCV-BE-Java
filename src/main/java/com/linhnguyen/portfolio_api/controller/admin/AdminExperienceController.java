package com.linhnguyen.portfolio_api.controller.admin;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.request.ExperienceCreateDTO;
import com.linhnguyen.portfolio_api.dto.request.ExperienceUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.ExperienceResponseDTO;
import com.linhnguyen.portfolio_api.service.ExperienceService;
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
 * REST Controller xử lý các API quản lý Professional Experience dành cho Admin.
 * Các endpoint này yêu cầu xác thực và phân quyền Admin.
 * Được tách riêng để dễ dàng cấu hình Spring Security.
 */
@RestController
@RequestMapping("/v1/admin/experiences")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin - Experiences", description = "Admin professional experience management API (Requires JWT authentication)")
@SecurityRequirement(name = "bearerAuth")
public class AdminExperienceController {

    private final ExperienceService experienceService;

    /**
     * Tạo mới experience.
     * Chỉ Admin mới có quyền tạo experience mới.
     *
     * @param request DTO chứa thông tin experience cần tạo
     * @return Thông tin experience vừa tạo với status 201 Created
     */
    @PostMapping
    @Operation(summary = "Create Experience", description = "Admin creates a new professional experience")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Experience created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate experience (same job title and company)")
    })
    public ResponseEntity<ApiResponse<ExperienceResponseDTO>> createExperience(
            @Valid @RequestBody ExperienceCreateDTO request) {
        log.info("[ADMIN] Request to create new experience: {} at {}", request.getJobTitle(), request.getCompany());
        ExperienceResponseDTO experience = experienceService.createExperience(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(experience));
    }

    /**
     * Cập nhật thông tin experience.
     * Chỉ Admin mới có quyền cập nhật thông tin experience.
     *
     * @param id      ID của experience cần cập nhật
     * @param request DTO chứa thông tin cập nhật
     * @return Thông tin experience sau khi cập nhật
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update Experience", description = "Admin updates professional experience information by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Experience updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Experience not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicate experience (same job title and company)")
    })
    public ResponseEntity<ApiResponse<ExperienceResponseDTO>> updateExperience(
            @Parameter(description = "Experience ID", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ExperienceUpdateDTO request) {
        log.info("[ADMIN] Request to update experience with ID: {}", id);
        ExperienceResponseDTO experience = experienceService.updateExperience(id, request);
        return ResponseEntity.ok(ApiResponse.success("Experience updated successfully", experience));
    }

    /**
     * Xóa experience (soft delete).
     * Chỉ Admin mới có quyền xóa experience.
     *
     * @param id ID của experience cần xóa
     * @return Thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Experience", description = "Admin soft-deletes a professional experience by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Experience deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Experience not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteExperience(
            @Parameter(description = "Experience ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("[ADMIN] Request to delete experience with ID: {}", id);
        experienceService.deleteExperience(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Experience deleted successfully"));
    }
}

