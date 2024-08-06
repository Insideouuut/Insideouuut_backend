package com.goorm.insideout.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReportRequest {
	@NotBlank(message = "신고 사유를 입력해주세요")
	private String reason;
}
