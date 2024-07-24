package com.goorm.insideout.club.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goorm.insideout.user.domain.User;

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

	private String date;

	private String region;

	@JsonIgnore
	private String question;


	private Integer memberLimit;
	Integer memberCunt;
	Integer price;
	private Integer ageLimit;


	@ManyToOne
	@JoinColumn(name = "owner_id")
	User owner;

	@OneToMany(mappedBy = "club",fetch = FetchType.LAZY)
	@Builder.Default
	List<ClubUser> members = new ArrayList<>();

	private String clubImg;

}
