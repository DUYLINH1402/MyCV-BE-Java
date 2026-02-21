package com.linhnguyen.portfolio_api.controller;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.response.ProfileResponseDTO;
import com.linhnguyen.portfolio_api.service.ProfileService;
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
@RequestMapping("/v1/profile")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Profile", description = "API công khai xem thông tin chủ sở hữu Portfolio")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Lấy thông tin profile của chủ sở hữu Portfolio.
     * Đây là API công khai, không cần xác thực.
     *
     * @return Thông tin profile
     */
    @GetMapping
    @Operation(summary = "Lấy thông tin Profile", description = "Lấy thông tin profile của chủ sở hữu Portfolio. Không yêu cầu xác thực.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy profile thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy profile")
    })
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getProfile() {
        ProfileResponseDTO profile = profileService.getOwnerProfile();
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
}

