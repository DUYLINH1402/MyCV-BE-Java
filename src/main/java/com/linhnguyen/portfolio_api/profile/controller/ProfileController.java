package com.linhnguyen.portfolio_api.profile.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.profile.dto.ProfileResponseDTO;
import com.linhnguyen.portfolio_api.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller xử lý API công khai lấy thông tin Profile.
 * Chỉ có 1 endpoint GET để hiển thị thông tin chủ sở hữu Portfolio.
 * Portfolio chỉ có DUY NHẤT 1 profile (chủ sở hữu).
 */
@RestController
@RequestMapping("/v1/public/profile")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Profile", description = "Public API to view portfolio owner's personal information")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Lấy thông tin profile của chủ sở hữu Portfolio.
     * Đây là API công khai, không cần xác thực.
     *
     * @return Profile information
     */
    @GetMapping
    @Operation(summary = "Get Profile", description = "Retrieve the portfolio owner's profile information. No authentication required.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getProfile() {
        ProfileResponseDTO profile = profileService.getOwnerProfile();
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
}

