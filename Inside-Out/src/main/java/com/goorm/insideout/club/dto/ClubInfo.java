package com.goorm.insideout.club.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.user.domain.User;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

public interface ClubInfo {

	Long getClubId();

	String getClubName();

	String getCategory();

	LocalDateTime getModifiedAt();
	String getContent();

	String getDate();

	String getRegion();
	String getQuestion();


	Integer getMemberLimit();
	Integer getMemberCunt();
	Integer getPrice();
	Integer getAgeLimit();


	String getClubImg();
}
