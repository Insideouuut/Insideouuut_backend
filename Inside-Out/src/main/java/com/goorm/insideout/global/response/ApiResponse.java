package com.goorm.insideout.global.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Metadata metadata; // 주로 결과의 개수를 담는다.

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<T> results; // 여러 개의 결과를 반환할 때 사용한다.

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private T data; // 단일 데이터 객체나 기타 응답 데이터를 담는 필드이다.

	public ApiResponse(List<T> results) {
		this.metadata = new Metadata(results.size());
		this.results = results;
	}

	public ApiResponse(T data) {
		this.data = data;
	}

	@Getter
	@AllArgsConstructor
	private static class Metadata {
		private int resultCount = 0;
	}
}