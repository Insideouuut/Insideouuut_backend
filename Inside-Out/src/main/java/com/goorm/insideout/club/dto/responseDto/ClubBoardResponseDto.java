package com.goorm.insideout.club.dto.responseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.image.dto.response.ImageResponse;
import com.goorm.insideout.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubBoardResponseDto {
	private Long clubId;

	private String category;
	private String categoryDetail;//
	private String level;//

	private Boolean hasMembershipFee;//
	private Integer membershipFeeAmount;

	private String date;

	private Integer participantLimit;
	private Integer memberCount;

	private String hasGenderRatio;//
	private String ratio;//

	private List<Integer> ageRange;//

	private String name;

	private String introduction;

	private Set<String> rules;//


	private List<ImageResponse> images;


	private Set<String> joinQuestions;//


	private LocalDateTime createdAt;


	private String activityRegion;



	private Long chatRoomId;

	private Boolean isHost = false;

	private Boolean isRecruiting = false;


	public static ClubBoardResponseDto of(Club club, User user){
		ClubBoardResponseDto res = new ClubBoardResponseDto();

		res.setClubId(club.getClubId());
		res.setName(club.getClubName());
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
		res.setMemberCount(club.getMemberCount());
		res.setHasGenderRatio(club.getHasGenderRatio());
		res.setRatio(club.getRatio());
		res.setAgeRange(club.getAgeRange());
		res.setName(club.getClubName());
		res.setIntroduction(club.getContent());
		res.setRules(club.getRules());
		res.setJoinQuestions(club.getJoinQuestions());
		res.setCreatedAt(club.getCreatedAt());
		res.setActivityRegion(club.getRegion());


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
