package com.goorm.insideout.club.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goorm.insideout.chatroom.domain.ChatRoom;
import com.goorm.insideout.image.domain.ClubImage;
import com.goorm.insideout.like.domain.ClubLike;
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

	private LocalDateTime createdAt;

	private String content;

	private LocalDateTime date;

	private String region;

	@JsonIgnore
	private String question;

	private Integer memberLimit;

	private Integer memberCount;

	private Integer price;

	private Integer ageLimit;

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

	@OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
	private List<ClubLike> likes = new ArrayList<>();

	///챗룸 변수만들고 원투원으로 // 클럽서비스의 클럽만들기에 챗룸만들기 추가하고 챗룸아이디를 이 변수로 받기
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club")
	ChatRoom chatRoom;

	private Long chat_room_id;

	public void increaseMemberCount() {
		this.memberCount++;
	}
}
