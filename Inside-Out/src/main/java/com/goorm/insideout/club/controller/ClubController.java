package com.goorm.insideout.club.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.club.dto.ClubDetailUserDto;
import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubDetailResponseDto;
import com.goorm.insideout.club.dto.responseDto.ClubListResponseDto;
import com.goorm.insideout.club.service.ClubService;
import com.goorm.insideout.club.service.ClubUserService;
import com.goorm.insideout.club.dto.requestDto.ClubDeleteRequestDto;
import com.goorm.insideout.club.dto.ClubBoardDto;
import com.goorm.insideout.club.dto.requestDto.ClubRequestDto;
import com.goorm.insideout.club.dto.responseDto.ClubResponseDto;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

///apiresponse<여기안에 명시해서 내보내라>
// 컨트롤러에는 단군하게 들어오고 나가는거만 보이게하고 복잡한 로직같은거는 다 서비스에서 처리할수있도록
// 시큐리티컨피그에 permitall의 url에 로그인안한 유저도 접근할수있는 경로 적어놔라 /clubs랑 /clubs/{clubid}같은거
// 유저받는거 @AuthenticationPrincipal CustomUserDetails userDetails랑   userDetails.getUser로 받기

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClubController {


	private final ClubService clubService;
	private final ClubUserService clubUserService;
	private final UserService userService;

	@GetMapping("/clubs")
	public ApiResponse<List<ClubListResponseDto>> findAllClub() {

		return new ApiResponse<List<ClubListResponseDto>>(clubService.findAllClubDesc());
	}

/*
	@GetMapping("/clubs/{clubId}")
	public ApiResponse<ClubDetailResponseDto> findClubDetail(@PathVariable Long clubId, @AuthenticationPrincipal CustomUserDetails userDetails) {

		User user;
		Club club;

		try {
			user = userDetails.getUser();
			club = clubService.findByClubId(clubId);

			System.out.println("user = " + user);
			System.out.println("club = " + club);
			if (club == null) {
				return new ApiResponse<>(ErrorCode.CLUB_NOT_FOUND);
			}
		} catch (Exception exception) {
			System.out.println("exception = " + exception);
			return new ApiResponse<>(ErrorCode.INVALID_REQUEST);
		}
		return new ApiResponse<>((ClubDetailResponseDto.of(club, user)));    //(clubService.findByClubId(clubId));
	}

 */





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

		User owner;

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
