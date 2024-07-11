package com.goorm.insideout.global.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goorm.insideout.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
	private ErrorCode errorCode;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Metadata metadata; // 주로 결과의 개수를 담는다.

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<T> results; // 여러 개의 결과를 반환할 때 사용한다.

	public ApiResponse(List<T> results) {
		this.errorCode = ErrorCode.REQUEST_OK;
		this.metadata = new Metadata(results.size());
		this.results = results;
	}

	public ApiResponse(T data) {
		this.errorCode = ErrorCode.REQUEST_OK;
		this.metadata = new Metadata(1);
		this.results = List.of(data);
	}

	public ApiResponse(ErrorCode errorCode) {
		this.errorCode = errorCode;

	}

	@Getter
	@AllArgsConstructor
	private static class Metadata {
		private int resultCount = 0;
	}
}