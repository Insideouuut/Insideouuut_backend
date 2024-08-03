package com.goorm.insideout.club.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.service.ClubService;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.user.domain.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClubController {


	private final ClubService clubService;



	@GetMapping("/clubs")
	public ApiResponse<List<ClubListResponseDto>> findByType(@RequestParam(name = "category") String category) {

		List<ClubListResponseDto> byCategory = clubService.findByCategory(category);
		System.out.println("byCategory.size = " + byCategory.size());
		for (ClubListResponseDto clubListResponseDto: byCategory){
			System.out.println("clubListResponseDto.getClubName() = " + clubListResponseDto.getClubName());;
		}
		System.out.println("clubService = " + clubService.findByCategory(category));
		return new ApiResponse<List<ClubListResponseDto>>(clubService.findByCategory(category));
	}




	@GetMapping("/clubs/{clubId}")
	public ApiResponse<ClubBoardResponseDto> findClubBoard(@PathVariable Long clubId) {


		return new ApiResponse<ClubBoardResponseDto>(ClubBoardResponseDto.of(clubService.findByClubId(clubId)));
	}

	@PostMapping("/clubs")
	public ApiResponse<ClubResponseDto> saveClub(@Valid @RequestBody ClubRequestDto clubRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails){

		User user;
		Club club;

		try {
			user = userDetails.getUser();
			
			club = clubService.createClub(clubRequestDto, /*clubRequestDto.getClubImg(), */ user);

		} catch (Exception exception) {
			return new ApiResponse<>(ErrorCode.CLUB_ALREADY_EXIST);
		}
		return new ApiResponse<ClubResponseDto>((ClubResponseDto.of(club.getClubId(), "클럽을 성공적으로 생성하였습니다.")));
	}


	@PutMapping("/clubs/{clubId}")
	public ApiResponse<ClubResponseDto> updateClub(@PathVariable Long clubId, @Valid @RequestBody ClubRequestDto clubRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {

		User user;
		Club club;

		try {
			user = userDetails.getUser();
			Long userId = user.getId();
			club = clubService.ownClub(clubId, userId);

			if(club==null) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_FOUND);
			}

			if (!club.getOwner().getEmail().equals(user.getEmail())) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_OWNER);
			}
			clubService.modifyClub(clubRequestDto, /*clubRequestDto.getClubImg(),*/ user, club);
		} catch (Exception exception) {
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}
		return new ApiResponse<ClubResponseDto>(ClubResponseDto.of(club.getClubId(), "클럽 상세 정보 수정이 완료되었습니다."));

	}

	@DeleteMapping("/clubs/{clubId}")
	public ApiResponse clubDelete(@PathVariable Long clubId, @AuthenticationPrincipal CustomUserDetails userDetails) {
		try {
			User user = userDetails.getUser();
			Long userId = user.getId();
			Club club = clubService.ownClub(clubId, userId);

			if(club==null) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_FOUND);
			}

			if (!club.getOwner().getEmail().equals(user.getEmail())) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_OWNER);
			}
			clubService.deleteClub(clubId);
		} catch (Exception exception) {
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}
		return new ApiResponse<>(ErrorCode.REQUEST_OK);
	}

}
