package com.linhnguyen.portfolio_api.profile.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.profile.dto.ProfileUpdateDTO;
import com.linhnguyen.portfolio_api.profile.dto.ProfileResponseDTO;
import com.linhnguyen.portfolio_api.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller xử lý các API quản lý thông tin Profile dành cho Admin.
 * Các endpoint này yêu cầu xác thực và phân quyền Admin.
 *
 * Lưu ý: Portfolio chỉ có DUY NHẤT 1 profile (chủ sở hữu).
 * Không có chức năng tạo mới profile - chỉ có cập nhật thông tin.
 */
@RestController
@RequestMapping("/v1/admin/profile")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin - Profile", description = "Admin profile management API (Requires JWT authentication)")
@SecurityRequirement(name = "bearerAuth")
public class AdminProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "Get Profile", description = "Retrieve current profile information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getProfile() {
        log.info("[ADMIN] Request to get profile information");
        ProfileResponseDTO profile = profileService.getOwnerProfile();
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping
    @Operation(summary = "Update Profile", description = "Admin updates profile information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token")
    })
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> updateProfile(
            @Valid @RequestBody ProfileUpdateDTO request) {
        log.info("[ADMIN] Request to update profile");
        ProfileResponseDTO profile = profileService.updateOwnerProfile(request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", profile));
    }

    @DeleteMapping("/cache")
    @Operation(summary = "Clear Profile Cache", description = "Clear all profile cache to refresh data")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cache cleared successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated or invalid token")
    })
    public ResponseEntity<ApiResponse<Void>> refreshCache() {
        log.info("[ADMIN] Request to clear profile cache");
        profileService.refreshProfileCache();
        return ResponseEntity.ok(ApiResponse.successMessage("All profile cache has been cleared"));
    }
}

