package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goorm.insideout.club.entity.Category;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.GenderRatio;
import com.goorm.insideout.club.entity.Level;
import com.goorm.insideout.user.dto.response.HostResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubListResponseDto {
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
	private HostResponse host;
	private List<Integer> ageRange;
	private List<String> rules;
	private List<String> joinQuestions;



	public static ClubListResponseDto of(Club club){
		return new ClubListResponseDto(club);
	}

	public ClubListResponseDto(Club club) {
		this.id = club.getClubId();
		this.name = club.getClubName();
		this.introduction = club.getContent();
		this.type = "동아리";
		this.chatRoomId = club.getChat_room_id();
		this.activityRegion = club.getRegion();
		this.createdAt = club.getCreatedAt();
		this.hasMembershipFee = club.getHasMembershipFee();
		this.membershipFeeAmount = club.getPrice();
		this.category = club.getCategory();
		this.categoryDetail = club.getCategoryDetail();;
		this.level = club.getLevel();
		this.date = club.getDate();
		this.participantLimit = club.getMemberLimit();
		this.participantNumber = club.getMemberCount();
		this.genderRatio = club.getGenderRatio();
		this.host = HostResponse.of(club.getOwner());
		this.ageRange = List.of(club.getMinAge(), club.getMaxAge());
		this.rules = new ArrayList<>(club.getRules());
		this.joinQuestions = new ArrayList<>(club.getJoinQuestions());

		if(participantLimit>participantNumber){
			this.isRecruiting = true;
		}

	}
}