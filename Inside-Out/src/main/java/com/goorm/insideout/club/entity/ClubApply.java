package com.goorm.insideout.club.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
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
public class ClubApply {

	@Id
	@GeneratedValue
	@Column(name = "apply_Id")
	private Long applyId;


	@NotNull
	@Column(name = "user_id")
	Long userId;


	@NotNull
	@Column(name = "club_id")
	Long clubId;

	private String userName;
	//private String profileImgUrl;
	private BigDecimal mannerTemp;
	private String answer;


	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "profile_image_id")
	private ProfileImage profileImage;




}
