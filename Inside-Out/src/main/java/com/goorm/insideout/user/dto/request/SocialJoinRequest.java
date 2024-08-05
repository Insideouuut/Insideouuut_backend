package com.goorm.insideout.user.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;

@Getter
public class SocialJoinRequest {
	private String nickName;

	private String phoneNumber;

	private String gender;

	private List<String> category;

	private LocalDate birthDate;
}
