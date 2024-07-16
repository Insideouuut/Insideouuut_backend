package com.goorm.insideout.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserJoinRequestDTO {
	@NotBlank(message = "email은 필수 입니다")
	@Email(message = "email은 올바른 email 형식 이여야 합니다")
	private String email;

	@NotBlank(message = "비밀번호는 필수 입니다")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상 영어와 숫자를 포함 해야합니다")
	private String password;

	@NotBlank(message = "이름은 필수 입니다")
	private String name;
}
