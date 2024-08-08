package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.image.dto.response.ImageResponse;
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
	private Boolean isHost = false;
	private String activityRegion;
	private LocalDateTime createdAt;
	private int view;
	private int like;
	private Boolean hasMembershipFee;
	private Integer membershipFeeAmount;
	private Boolean isRecruiting = false;
	private String level;
	private String category;
	private String categoryDetail;
	private String date;
	private Integer participantNumber;
	private Integer participantLimit;
	private String ratio;
	private List<Integer> ageRange;
	private List<String> rules;
	private List<String> joinQuestions;
	private HostResponse host;
	private List<ImageResponse> images;

	public static ClubBoardResponseDto of(Club club, User user){
		ClubBoardResponseDto res = new ClubBoardResponseDto();

		res.setId(club.getClubId());
		res.setName(club.getClubName());
		res.type = "동아리";
		res.images = club.getImages()
			.stream()
			.map(image -> ImageResponse.from(image.getImage()))
			.toList();
		res.setCategory(club.getCategory());
		res.setCategoryDetail(club.getCategoryDetail());
		res.setLevel(club.getLevel());
		res.setHasMembershipFee(club.getHasMembershipFee());
		res.setMembershipFeeAmount(club.getPrice());
		res.setDate(club.getDate());
		res.setParticipantLimit(club.getMemberLimit());
		res.setParticipantNumber(club.getMemberCount());
		res.setRatio(club.getRatio());
		res.setAgeRange(club.getAgeRange());
		res.setName(club.getClubName());
		res.setIntroduction(club.getContent());
		res.setRules(new ArrayList<>(club.getRules()));
		res.setJoinQuestions(new ArrayList<>(club.getJoinQuestions()));
		res.setCreatedAt(club.getCreatedAt());
		res.setActivityRegion(club.getRegion());
		res.setHost(HostResponse.of(user));

		res.setChatRoomId(club.getChat_room_id());

		if(club.getOwner().getId().equals(user.getId())){
			res.setIsHost(true);
		}

		if(club.getMemberLimit() > club.getMemberCount()){
			res.setIsRecruiting(true);
		}
		return res;
	}


}