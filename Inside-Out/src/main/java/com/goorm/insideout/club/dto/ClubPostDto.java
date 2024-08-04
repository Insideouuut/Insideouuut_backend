package com.goorm.insideout.club.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubPostDto {
	private Long id;

	@NotBlank(message = "제목을 입력해주세요.")
	private String title;

	private LocalDateTime dateTime;

	private String writer;

	@NotBlank(message = "내용을 입력하세요.")
	private String content;

	private ClubUserDto clubUserDto;
}
