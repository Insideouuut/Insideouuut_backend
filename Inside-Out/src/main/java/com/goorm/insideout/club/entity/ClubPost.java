package com.goorm.insideout.club.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goorm.insideout.club.dto.requestDto.ClubPostRequestDto;
import com.goorm.insideout.image.domain.ClubPostImage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class ClubPost {


	@Id
	@GeneratedValue
	@Column(name = "clubPost_Id")
	private Long Id;

	private String postTitle;

	private String category;

	private String writer;

	private LocalDateTime createTime;
	private String postContent;


	@OneToMany(mappedBy = "clubPost",cascade = CascadeType.ALL)
	@Builder.Default
	@JsonIgnore
	private List<ClubComment> comments = new ArrayList<>();


	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clubUser_Id")
	private ClubUser clubUser;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "club_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	Club club;

	@OneToMany(mappedBy = "clubPost", cascade = CascadeType.ALL)
	private List<ClubPostImage> images = new ArrayList<>();

	public void update(ClubPostRequestDto clubPostRequestDto){
		this.postTitle=clubPostRequestDto.getPostTitle();
		this.category =clubPostRequestDto.getCategory();
		this.postContent = clubPostRequestDto.getPostContent();
	}


}
