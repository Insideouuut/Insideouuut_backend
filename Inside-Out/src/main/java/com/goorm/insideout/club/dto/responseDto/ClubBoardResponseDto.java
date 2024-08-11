package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.club.entity.Category;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.GenderRatio;
import com.goorm.insideout.club.entity.Level;
import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.meeting.dto.response.MeetingResponse;
import com.querydsl.core.annotations.QueryProjection;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.dto.response.HostResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubBoardResponseDto {
	private Long id;
	private String name;
	private String introduction;
	private String type;
	private Long chatRoomId;
	private String activityRegion;
	private LocalDateTime createdAt;
	private int view;
	private int like;
	private Boolean hasMembershipFee;
	private Integer membershipFeeAmount;
	private Boolean isRecruiting = false;
	private Level level;
	private Category category;
	private String categoryDetail;
	private String date;
	private Integer participantNumber;
	private Integer participantLimit;
	private GenderRatio genderRatio;
	private List<Integer> ageRange;
	private List<String> rules;
	private List<String> joinQuestions;
	private HostResponse host;
	private List<ImageResponse> images;
	private List<MeetingResponse> meetings;

	@QueryProjection
	public ClubBoardResponseDto(Club club) {
		this.id = club.getClubId();
		this.name = club.getClubName();
		this.introduction = club.getContent();
		this.type = "동아리";
		this.images = club.getImages()
			.stream()
			.map(image -> ImageResponse.from(image.getImage()))
			.toList();
		this.category = club.getCategory();
		this.categoryDetail = club.getCategoryDetail();
		this.level = club.getLevel();
		this.hasMembershipFee = club.getHasMembershipFee();
		this.membershipFeeAmount = club.getPrice();
		this.date = club.getDate();
		this.participantLimit = club.getMemberLimit();
		this.participantNumber = club.getMemberCount();
		this.genderRatio = club.getGenderRatio();
		this.ageRange = List.of(club.getMinAge(), club.getMaxAge());
		this.name = club.getClubName();
		this.introduction = club.getContent();
		this.rules = new ArrayList<>(club.getRules());
		this.joinQuestions = new ArrayList<>(club.getJoinQuestions());
		this.createdAt = club.getCreatedAt();
		this.activityRegion = club.getRegion();
		this.host = HostResponse.of(club.getOwner());
		this.chatRoomId = club.getChat_room_id();
		this.meetings = club.getMeetings()
			.stream()
			.map(MeetingResponse::of)
			.toList();

		if(club.getMemberLimit() > club.getMemberCount()){
			this.isRecruiting = true;
		}
	}

	public static ClubBoardResponseDto of(Club club){
		return new ClubBoardResponseDto(club);
	}


}