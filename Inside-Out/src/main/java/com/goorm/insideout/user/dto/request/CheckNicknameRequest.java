package com.goorm.insideout.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CheckNicknameRequest {
	@NotBlank(message = "이름은 필수 입니다")
	private String nickname;
}
