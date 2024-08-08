package com.goorm.insideout.club.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.image.domain.ClubImage;
import com.goorm.insideout.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Club {

	@Id
	@GeneratedValue
	@Column(name = "club_Id")
	@JsonIgnore
	private Long clubId;

	private String clubName;

	private String category;
	private String categoryDetail;//
	private String level;//

	private LocalDateTime createdAt;

	private String content;

	private String date;

	private String region;

	private Set<String> joinQuestions;//


	private Integer memberLimit;

	private Integer memberCount;

	private Boolean hasMembershipFee;//
	private Integer price;

	private String hasGenderRatio;//
	private String ratio;//

	private List<Integer> ageRange;//

	private Set<String> rules;//


	@ManyToOne
	@JoinColumn(name = "owner_id")
	User owner;

	@OneToMany(mappedBy = "club",fetch = FetchType.LAZY)
	@Builder.Default
	List<ClubUser> members = new ArrayList<>();

	@OneToMany(mappedBy = "club",fetch = FetchType.LAZY)
	@JsonIgnore
	List<ClubPost> posts;

	@OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
	private List<ClubImage> images = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club")
	ChatRoom chatRoom;

	private Long chat_room_id;

	public void increaseMemberCount() {
		this.memberCount++;
	}
}
