package com.goorm.insideout.user.controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.image.service.ImageService;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.dto.request.ProfileUpdateRequest;
import com.goorm.insideout.user.dto.response.MyProfileResponse;
import com.goorm.insideout.user.dto.response.ProfileResponse;
import com.goorm.insideout.user.repository.UserRepository;
import com.goorm.insideout.user.service.UserProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "UserProfileController", description = "프로필 관련 API")
@RequestMapping("/api/users")
public class UserProfileController {
	private final UserProfileService userProfileService;
	private final UserRepository userRepository;

	@PatchMapping()
	@Operation(summary = "사용자 정보 수정 API", description = "프로필 정보를 수정할 수 있는 API 입니다.(닉에임과 비밀번호만 가능)")
	public ApiResponse updateUserProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		userProfileService.updateMyProfile(profileUpdateRequest, customUserDetails.getUser());
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@PatchMapping("/image")
	@Operation(summary = "사용자 이미지 수정 API", description = "프로필 이미지 수정할 수 있는 API 입니다.")
	public ApiResponse<String> updateUserProfileImage(@RequestPart("image") MultipartFile multipartFile,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		userProfileService.updateMyProfileImage(multipartFile, customUserDetails.getUser());
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

	@GetMapping()
	@Operation(summary = "사용자 프로필 확인 API", description = "사용자 프로필를 확인할 수 있는 API 입니다.")
	public ApiResponse<MyProfileResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		MyProfileResponse profile = userProfileService.getMyProfile(customUserDetails.getUser());

		return new ApiResponse<MyProfileResponse>(profile);
	}

	@PatchMapping("/{userId}")
	@Operation(summary = "특정 유저 프로필 확인 API", description = "사용자 프로필를 확인할 수 있는 API 입니다.")
	public ApiResponse<ProfileResponse> getUserProfile(@PathVariable Long userId,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw ModongException.from(ErrorCode.USER_NOT_FOUND);
		}
		ProfileResponse profile = userProfileService.getProfile(user.get());
		return new ApiResponse<ProfileResponse>(profile);
	}
}
