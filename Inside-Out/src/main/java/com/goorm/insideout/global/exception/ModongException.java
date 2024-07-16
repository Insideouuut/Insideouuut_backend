package com.goorm.insideout.global.exception;

import org.springframework.util.StringUtils;

import lombok.Getter;

@Getter
public class ModongException extends RuntimeException {

	private final ErrorCode errorCode;

	private String message;

	private ModongException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	private ModongException(ErrorCode errorCode,String message) {
		super(message);
		this.errorCode = errorCode;
		this.message = message;
	}

	public static ModongException from(ErrorCode errorCode) {
		return new ModongException(errorCode);
	}

	public static ModongException from(ErrorCode errorCode,String message) {
		return new ModongException(errorCode,message);
	}

	public String GetMessage() {
		if (StringUtils.hasLength(this.message)) {
			return this.message;
		}
		return errorCode.getMessage();
	}
}
