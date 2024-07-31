package com.goorm.insideout.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CheckEmailRequest {
	@NotBlank(message = "email은 필수 입니다")
	@Email(message = "올바른 email 형식이 아닙니다")
	private String email;
}
