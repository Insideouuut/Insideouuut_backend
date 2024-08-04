package com.goorm.insideout.club.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class ClubComment {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_Id")
	private Long id;

	@Column(name = "comment_content")
	private String content;

	private LocalDateTime dateTime;
	private LocalDateTime updateDateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clubPost_Id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ClubPost clubPost;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clubUser_Id")
	private ClubUser clubUser;



}
