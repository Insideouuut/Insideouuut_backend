package com.goorm.insideout.global.response;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
	private Status status;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Metadata metadata; // 주로 결과의 개수를 담는다.

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<T> results; // 여러 개의 결과를 반환할 때 사용한다.

	public ApiResponse(List<T> results) {
		this.status = new Status(ErrorCode.REQUEST_OK);
		this.metadata = new Metadata(results.size());
		this.results = results;
	}

	public ApiResponse(List<T> results, Pageable pageable) {
		this.status = new Status(ErrorCode.REQUEST_OK);
		this.metadata = new Metadata(results.size(), pageable);
		this.results = results;
	}

	public ApiResponse(T data) {
		this.status = new Status(ErrorCode.REQUEST_OK);
		this.metadata = new Metadata(1);
		this.results = List.of(data);
	}

	public ApiResponse(ErrorCode errorCode) {
		this.status = new Status(errorCode);
	}

	public ApiResponse(ModongException exception) {
		this.status = new Status(exception.getErrorCode());
	}

	@Getter
	@AllArgsConstructor
	private static class Metadata {
		private int resultCount = 0;
		private Pageable pageable;

		public Metadata(int resultCount) {
			this.resultCount = resultCount;
		}
	}

	@Getter
	private static class Status {
		private int code;
		private String message;

		public Status(ErrorCode errorCode) {
			this.code = errorCode.getStatus().value();
			this.message = errorCode.getMessage();
		}
	}
}
