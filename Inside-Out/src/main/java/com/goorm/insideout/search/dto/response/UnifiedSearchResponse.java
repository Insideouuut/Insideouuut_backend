package com.goorm.insideout.search.dto.response;

import java.util.List;

import com.goorm.insideout.club.dto.responseDto.ClubBoardResponseDto;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UnifiedSearchResponse {
	private List<ClubBoardResponseDto> clubSearchResults;
	private List<MeetingResponse> meetingSearchResults;

	public static UnifiedSearchResponse of(List<ClubBoardResponseDto> clubSearchResults, List<MeetingResponse> meetingSearchResults) {
		UnifiedSearchResponse unifiedSearchResponse = new UnifiedSearchResponse();

		unifiedSearchResponse.clubSearchResults = clubSearchResults;
		unifiedSearchResponse.meetingSearchResults = meetingSearchResults;

		return unifiedSearchResponse;
	}
}
