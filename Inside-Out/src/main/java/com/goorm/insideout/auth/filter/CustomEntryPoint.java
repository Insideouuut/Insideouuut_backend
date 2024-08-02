package com.goorm.insideout.auth.filter;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.response.ApiResponse;
import com.nimbusds.oauth2.sdk.OAuth2Error;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException e) throws IOException, ServletException {
		createAPIResponse(response, USER_NOT_AUTHENTICATED);
	}

	private void createAPIResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		ApiResponse apiResponse = new ApiResponse<>(errorCode);
		response.setStatus(errorCode.getStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), apiResponse);
	}
}
