package com.goorm.insideout.user.dto.request;

import org.springframework.web.bind.annotation.GetMapping;

import lombok.Getter;

@Getter
public class ProfileUpdateRequest {
	private String nickname;
	private String password;

}
