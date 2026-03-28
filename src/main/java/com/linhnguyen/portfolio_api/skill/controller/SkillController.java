package com.linhnguyen.portfolio_api.skill.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.skill.dto.SkillResponseDTO;
import com.linhnguyen.portfolio_api.skill.entity.SkillCategory;
import com.linhnguyen.portfolio_api.skill.service.SkillService;
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
 */
@RestController
@RequestMapping("/v1/public/skills")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Skills", description = "Public API to view portfolio skills")
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    @Operation(summary = "Get All Skills", description = "Retrieve all active skills. No authentication required.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Skills retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<SkillResponseDTO>>> getAllSkills() {
        List<SkillResponseDTO> skills = skillService.getAllSkills();
        return ResponseEntity.ok(ApiResponse.success(skills));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Skill by ID", description = "Retrieve detailed skill information by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Skill retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Skill not found")
    })
    public ResponseEntity<ApiResponse<SkillResponseDTO>> getSkillById(
            @Parameter(description = "Skill ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("Request to get skill with ID: {}", id);
        SkillResponseDTO skill = skillService.getSkillById(id);
        return ResponseEntity.ok(ApiResponse.success(skill));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get Skills by Category", description = "Retrieve skills filtered by category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Skills retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<SkillResponseDTO>>> getSkillsByCategory(
            @Parameter(description = "Skill category (FRONTEND, BACKEND, DATABASE, TOOLS, DEVOPS, OTHER)", example = "BACKEND", required = true)
            @PathVariable SkillCategory category) {
        log.info("Request to get skills by category: {}", category);
        List<SkillResponseDTO> skills = skillService.getSkillsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(skills));
    }
}

