package com.linhnguyen.portfolio_api.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.response.ExperienceResponseDTO;
import com.linhnguyen.portfolio_api.service.ExperienceService;
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
 * REST Controller xử lý các API công khai liên quan đến Professional Experience.
 * Chỉ chứa các endpoint GET để hiển thị kinh nghiệm làm việc trên Portfolio.
 * Các API quản lý (POST, PUT, DELETE) được tách riêng vào AdminExperienceController.
 */
@RestController
@RequestMapping("/v1/public/experiences")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Experiences", description = "Public API to view professional experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    /**
     * Lấy danh sách tất cả kinh nghiệm làm việc.
     * Sắp xếp theo startDate giảm dần (gần nhất hiển thị trước).
     *
     * @return Danh sách experience đang hoạt động
     */
    @GetMapping
    @Operation(summary = "Get All Experiences",
            description = "Retrieve all professional experiences sorted by start date descending. No authentication required.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Experiences retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ExperienceResponseDTO>>> getAllExperiences() {
//        log.info("Request to get all experiences");
        List<ExperienceResponseDTO> experiences = experienceService.getAllExperiences();
        return ResponseEntity.ok(ApiResponse.success(experiences));
    }

    /**
     * Lấy thông tin experience theo ID.
     *
     * @param id ID của experience cần lấy
     * @return Thông tin experience
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get Experience by ID",
            description = "Retrieve detailed professional experience information by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Experience retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Experience not found")
    })
    public ResponseEntity<ApiResponse<ExperienceResponseDTO>> getExperienceById(
            @Parameter(description = "Experience ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("Request to get experience with ID: {}", id);
        ExperienceResponseDTO experience = experienceService.getExperienceById(id);
        return ResponseEntity.ok(ApiResponse.success(experience));
    }
}

