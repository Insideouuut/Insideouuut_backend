package com.goorm.insideout.global.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.global.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger("ErrorLogger");
	private static final String LOG_FORMAT_INFO = "\n[🔵INFO] - ({} {})\n(id: {}, role: {})\n{}\n {}: {}";
	private static final String LOG_FORMAT_WARN = "\n[🟠WARN] - ({} {})\n(id: {}, role: {})";
	private static final String LOG_FORMAT_ERROR = "\n[🔴ERROR] - ({} {})\n(id: {}, role: {})";

	// INFO 출력 예시
    /*
        [🔵INFO] - (POST /admin/info)
        (id: 1, role: MEMBER)
        FOR_TEST_ERROR
         com.festago.exception.BadRequestException: 테스트용 에러입니다.
     */

	// WARN 출력 예시
    /*
        [🟠WARN] - (POST /admin/warn)
        (id: 1, role: MEMBER)
        FOR_TEST_ERROR
         com.festago.exception.InternalServerException: 테스트용 에러입니다.
          at com.festago.admin.presentation.AdminController.getWarn(AdminController.java:129)
     */

	// ERROR 출력 예시
    /*
        [🔴ERROR] - (POST /admin/error)
        (id: 1, role: MEMBER)
         java.lang.IllegalArgumentException: 테스트용 에러입니다.
          at com.festago.admin.presentation.AdminController.getError(AdminController.java:129)
     */

	@ExceptionHandler(RuntimeException.class)
	public ApiResponse<Void> handle(ModongException exception, HttpServletRequest request) {
		logInfo(exception, request);
		return new ApiResponse<>(exception);
	}

	private void logInfo(ModongException e, HttpServletRequest request) {
		log.info(LOG_FORMAT_INFO, request.getMethod(), request.getRequestURI(), e.getErrorCode(), e.getClass().getName(), e.getMessage());
	}

	private void logWarn(ModongException e, HttpServletRequest request) {
		log.warn(LOG_FORMAT_WARN, request.getMethod(), request.getRequestURI(), e);
	}

	private void logError(Exception e, HttpServletRequest request) {
		log.error(LOG_FORMAT_ERROR, request.getMethod(), request.getRequestURI(), e);
	}
}
