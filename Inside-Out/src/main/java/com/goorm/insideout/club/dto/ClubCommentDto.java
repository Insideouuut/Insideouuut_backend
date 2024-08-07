package com.goorm.insideout.club.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubCommentDto {
	private Long id;
	private String writer;
	private String comment;

	private LocalDateTime dateTime;

}
