package com.linhnguyen.portfolio_api.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.response.SkillResponseDTO;
import com.linhnguyen.portfolio_api.entity.SkillCategory;
import com.linhnguyen.portfolio_api.service.SkillService;
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
 * REST Controller xử lý các API công khai liên quan đến Skill.
 * Chỉ chứa các endpoint GET để hiển thị thông tin trên Portfolio.
 * Các API quản lý (POST, PUT, DELETE) được tách riêng vào AdminSkillController.
 */
@RestController
@RequestMapping("/v1/skills")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Skills", description = "API công khai xem kỹ năng")
public class SkillController {

    private final SkillService skillService;

    /**
     * Lấy danh sách tất cả skill.
     *
     * @return Danh sách skill đang hoạt động
     */
    @GetMapping
    @Operation(summary = "Lấy tất cả skills", description = "Lấy danh sách tất cả skill đang hoạt động. Không yêu cầu xác thực.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    public ResponseEntity<ApiResponse<List<SkillResponseDTO>>> getAllSkills() {
        log.info("Request lấy danh sách tất cả skills");
        List<SkillResponseDTO> skills = skillService.getAllSkills();
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    /**
     * Lấy thông tin skill theo ID.
     *
     * @param id ID của skill cần lấy
     * @return Thông tin skill
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy skill theo ID", description = "Lấy thông tin chi tiết của skill theo ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy skill thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy skill")
    })
    public ResponseEntity<ApiResponse<SkillResponseDTO>> getSkillById(
            @Parameter(description = "ID của skill", example = "1", required = true)
            @PathVariable Long id) {
        log.info("Request lấy skill với ID: {}", id);
        SkillResponseDTO skill = skillService.getSkillById(id);
        return ResponseEntity.ok(ApiResponse.success(skill));
    }

    /**
     * Lấy danh sách skill theo danh mục.
     *
     * @param category Danh mục cần lọc (FRONTEND, BACKEND, DATABASE, TOOLS, DEVOPS, OTHER)
     * @return Danh sách skill thuộc danh mục đó
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Lấy skills theo danh mục", description = "Lấy danh sách skill theo danh mục phân loại")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    public ResponseEntity<ApiResponse<List<SkillResponseDTO>>> getSkillsByCategory(
            @Parameter(description = "Danh mục kỹ năng (FRONTEND, BACKEND, DATABASE, TOOLS, DEVOPS, OTHER)", example = "BACKEND", required = true)
            @PathVariable SkillCategory category) {
        log.info("Request lấy skills theo danh mục: {}", category);
        List<SkillResponseDTO> skills = skillService.getSkillsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(skills));
    }
}
