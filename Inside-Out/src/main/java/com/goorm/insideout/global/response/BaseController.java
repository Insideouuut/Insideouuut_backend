package com.goorm.insideout.global.response;

import java.util.List;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.goorm.insideout.global.exception.ModongException;

import jakarta.servlet.http.HttpServletResponse;

abstract public class BaseController {
	@ExceptionHandler(ModongException.class)
	public <T> ApiResponse<T> customExceptionHandler(HttpServletResponse response, ModongException customException) {
		response.setStatus(customException.getErrorCode().getStatus().value());

		return new ApiResponse<T>(
			customException.getErrorCode());
	}

	public <T> ApiResponse<T> makeAPIResponse(List<T> result) {
		return new ApiResponse<>(result);
	}

	public <T> ApiResponse<T> makeAPIResponse(T result) {
		return new ApiResponse<>(result);
	}
}
