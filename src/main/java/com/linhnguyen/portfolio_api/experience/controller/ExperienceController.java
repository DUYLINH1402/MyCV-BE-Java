package com.linhnguyen.portfolio_api.experience.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.experience.dto.ExperienceResponseDTO;
import com.linhnguyen.portfolio_api.experience.service.ExperienceService;
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
 */
@RestController
@RequestMapping("/v1/public/experiences")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Experiences", description = "Public API to view professional experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping
    @Operation(summary = "Get All Experiences",
            description = "Retrieve all professional experiences sorted by start date descending. No authentication required.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Experiences retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<ExperienceResponseDTO>>> getAllExperiences() {
        List<ExperienceResponseDTO> experiences = experienceService.getAllExperiences();
        return ResponseEntity.ok(ApiResponse.success(experiences));
    }

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

