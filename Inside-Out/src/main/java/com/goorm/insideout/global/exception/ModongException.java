package com.goorm.insideout.global.exception;

import lombok.Getter;

@Getter
public class ModongException extends RuntimeException {

	private final ErrorCode errorCode;

	private ModongException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public static ModongException from(ErrorCode errorCode) {
		return new ModongException(errorCode);
	}
}