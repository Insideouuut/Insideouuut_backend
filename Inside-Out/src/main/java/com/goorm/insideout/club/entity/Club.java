package com.goorm.insideout.club.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.image.domain.ClubImage;
import com.goorm.insideout.like.domain.ClubLike;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Club {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_Id")
	@JsonIgnore
	private Long clubId;

	private String clubName;

	@Enumerated(EnumType.STRING)
	private Category category;

	private String categoryDetail;//

	@Enumerated(EnumType.STRING)
	private Level level;//

	private LocalDateTime createdAt;

	private String content;

	private String date;

	private String region;

	@ElementCollection
	@CollectionTable(name = "club_join_questions", joinColumns = @JoinColumn(name = "club_id"))
	@Column(name = "join_question")
	@Builder.Default
	private List<String> joinQuestions = new ArrayList<>();//


	private Integer memberLimit;

	private Integer memberCount;

	private Boolean hasMembershipFee;//
	private Integer price;

	@Enumerated(EnumType.STRING)
	private GenderRatio genderRatio;//

	private int minAge;
	private int maxAge;

	@ElementCollection
	@CollectionTable(name = "club_rules", joinColumns = @JoinColumn(name = "club_id"))
	@Column(name = "rule")
	@Builder.Default
	private Set<String> rules = new HashSet<>();//


	@ManyToOne
	@JoinColumn(name = "owner_id")
	User owner;

	@OneToMany(mappedBy = "club",fetch = FetchType.LAZY)
	@Builder.Default
	List<ClubUser> members = new ArrayList<>();

	@OneToMany(mappedBy = "club",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	List<ClubPost> posts;

	@OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
	@Builder.Default
	private List<ClubImage> images = new ArrayList<>();

	@OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
	private List<ClubLike> likes = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "club")
	ChatRoom chatRoom;

	@OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
	private List<Meeting> meetings = new ArrayList<>();

	private Long chat_room_id;

	public void increaseMemberCount() {
		this.memberCount++;
	}

}