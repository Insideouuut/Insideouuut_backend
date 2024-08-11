package com.goorm.insideout.club.dto.requestDto;

import java.util.List;

import com.goorm.insideout.club.dto.AnswerDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClubApplyRequestDto {

	private List<AnswerDto> answers;
}
