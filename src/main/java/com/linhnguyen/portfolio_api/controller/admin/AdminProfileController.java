package com.linhnguyen.portfolio_api.controller.admin;

import com.linhnguyen.portfolio_api.common.ApiResponse;
import com.linhnguyen.portfolio_api.dto.request.ProfileUpdateDTO;
import com.linhnguyen.portfolio_api.dto.response.ProfileResponseDTO;
import com.linhnguyen.portfolio_api.service.ProfileService;
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
@Tag(name = "Admin - Profile", description = "API quản lý thông tin cá nhân dành cho Admin (Yêu cầu xác thực)")
@SecurityRequirement(name = "bearerAuth")
public class AdminProfileController {

    private final ProfileService profileService;

    /**
     * Lấy thông tin profile hiện tại.
     *
     * @return Thông tin profile
     */
    @GetMapping
    @Operation(summary = "Lấy thông tin profile", description = "Lấy thông tin profile hiện tại")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lấy profile thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Không tìm thấy profile")
    })
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getProfile() {
        log.info("[ADMIN] Request lấy thông tin profile");
        ProfileResponseDTO profile = profileService.getOwnerProfile();
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    /**
     * Cập nhật thông tin profile.
     * Chỉ Admin mới có quyền cập nhật.
     *
     * @param request DTO chứa thông tin cập nhật
     * @return Thông tin profile sau khi cập nhật
     */
    @PutMapping
    @Operation(summary = "Cập nhật profile", description = "Admin cập nhật thông tin profile")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cập nhật profile thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ")
    })
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> updateProfile(
            @Valid @RequestBody ProfileUpdateDTO request) {
        log.info("[ADMIN] Request cập nhật profile");
        ProfileResponseDTO profile = profileService.updateOwnerProfile(request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật profile thành công", profile));
    }

    /**
     * Xóa toàn bộ cache profile.
     * Sử dụng sau khi migration hoặc thay đổi schema để làm mới dữ liệu cache.
     *
     * @return Thông báo xóa cache thành công
     */
    @DeleteMapping("/cache")
    @Operation(summary = "Xóa cache profile", description = "Xóa toàn bộ cache profile để làm mới dữ liệu")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Xóa cache thành công"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Chưa xác thực hoặc token không hợp lệ")
    })
    public ResponseEntity<ApiResponse<Void>> refreshCache() {
        log.info("[ADMIN] Request xóa cache profile");
        profileService.refreshProfileCache();
        return ResponseEntity.ok(ApiResponse.success("Đã xóa toàn bộ cache profile", null));
    }
}

