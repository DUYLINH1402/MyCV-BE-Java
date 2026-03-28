package com.linhnguyen.portfolio_api.skill.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.skill.dto.SkillCreateDTO;
import com.linhnguyen.portfolio_api.skill.dto.SkillUpdateDTO;
import com.linhnguyen.portfolio_api.skill.dto.SkillResponseDTO;
import com.linhnguyen.portfolio_api.skill.service.SkillService;
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
 */
@RestController
@RequestMapping("/v1/admin/skills")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin - Skills", description = "Admin skill management API (Requires JWT authentication)")
@SecurityRequirement(name = "bearerAuth")
public class AdminSkillController {

    private final SkillService skillService;

    @PostMapping
    @Operation(summary = "Create Skill", description = "Admin creates a new skill")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Skill created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid data or skill already exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token")
    })
    public ResponseEntity<ApiResponse<SkillResponseDTO>> createSkill(
            @Valid @RequestBody SkillCreateDTO request) {
        log.info("[ADMIN] Request to create new skill");
        SkillResponseDTO skill = skillService.createSkill(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(skill));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Skill", description = "Admin updates skill information by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Skill updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Skill not found")
    })
    public ResponseEntity<ApiResponse<SkillResponseDTO>> updateSkill(
            @Parameter(description = "Skill ID", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody SkillUpdateDTO request) {
        log.info("[ADMIN] Request to update skill with ID: {}", id);
        SkillResponseDTO skill = skillService.updateSkill(id, request);
        return ResponseEntity.ok(ApiResponse.success("Skill updated successfully", skill));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Skill", description = "Admin soft-deletes a skill by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Skill deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Skill not found")
    })
    public ResponseEntity<ApiResponse<Void>> deleteSkill(
            @Parameter(description = "Skill ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("[ADMIN] Request to delete skill with ID: {}", id);
        skillService.deleteSkill(id);
        return ResponseEntity.ok(ApiResponse.successMessage("Skill deleted successfully"));
    }
}

