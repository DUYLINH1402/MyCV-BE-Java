package com.linhnguyen.portfolio_api.controller.admin;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.request.SkillCreateDTO;
import com.linhnguyen.portfolio_api.dto.request.SkillUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.SkillResponseDTO;
import com.linhnguyen.portfolio_api.service.SkillService;
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
 * REST Controller xử lý các API quản lý Skill dành cho Admin.
 * Các endpoint này yêu cầu xác thực và phân quyền Admin.
 * Được tách riêng để dễ dàng cấu hình Spring Security sau này.
 */
@RestController
@RequestMapping("/v1/admin/skills")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin - Skills", description = "API quản lý skill dành cho Admin (Yêu cầu xác thực)")
@SecurityRequirement(name = "bearerAuth")
public class AdminSkillController {

    private final SkillService skillService;

    /**
     * Tạo mới skill.
     * Chỉ Admin mới có quyền tạo skill mới.
     *
     * @param request DTO chứa thông tin skill cần tạo
     * @return Thông tin skill vừa tạo với status 201 Created
     */
    @PostMapping
    @Operation(summary = "Tạo skill mới", description = "Admin tạo mới một skill")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Tạo skill thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc skill đã tồn tại"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ")
    })
    public ResponseEntity<ApiResponse<SkillResponseDTO>> createSkill(
            @Valid @RequestBody SkillCreateDTO request) {
        log.info("[ADMIN] Request tạo skill mới");
        SkillResponseDTO skill = skillService.createSkill(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(skill));
    }

    /**
     * Cập nhật thông tin skill.
     * Chỉ Admin mới có quyền cập nhật thông tin skill.
     *
     * @param id      ID của skill cần cập nhật
     * @param request DTO chứa thông tin cập nhật
     * @return Thông tin skill sau khi cập nhật
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật skill", description = "Admin cập nhật thông tin skill theo ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cập nhật skill thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy skill")
    })
    public ResponseEntity<ApiResponse<SkillResponseDTO>> updateSkill(
            @Parameter(description = "ID của skill", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody SkillUpdateDTO request) {
        log.info("[ADMIN] Request cập nhật skill với ID: {}", id);
        SkillResponseDTO skill = skillService.updateSkill(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật skill thành công", skill));
    }

    /**
     * Xóa skill (soft delete).
     * Chỉ Admin mới có quyền xóa skill.
     *
     * @param id ID của skill cần xóa
     * @return Thông báo xóa thành công
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa skill", description = "Admin xóa mềm skill theo ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Xóa skill thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy skill")
    })
    public ResponseEntity<ApiResponse<Void>> deleteSkill(
            @Parameter(description = "ID của skill", example = "1", required = true)
            @PathVariable Long id) {
        log.info("[ADMIN] Request xóa skill với ID: {}", id);
        skillService.deleteSkill(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa skill thành công", null));
    }
}

