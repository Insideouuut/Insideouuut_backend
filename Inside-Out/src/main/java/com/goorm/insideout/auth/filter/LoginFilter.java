package com.goorm.insideout.auth.filter;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goorm.insideout.auth.utils.JWTUtil;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.global.response.ApiResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;

	public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, String url) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl(url);
	}

	//로그인 시도시 작동
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		//request 받은 json 값을 추출함
		Map<String, String> loginInfo = getLoginInfoFromJson(request);
		String email = loginInfo.get("email");
		String password = loginInfo.get("password");
		//해당 값이 없을시 예외처리
		if (email == null) {
			throw ModongException.from(ErrorCode.INVALID_REQUEST, "이메일을 입력해 주세요");
		}
		if (password == null) {
			throw ModongException.from(ErrorCode.INVALID_REQUEST, "비밀번호를 입력해 주세요");
		}

		//스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
		//token에 담은 검증을 위한 AuthenticationManager로 전달
		return authenticationManager.authenticate(authToken);
	}

	//로그인 성공시 작동 - 토큰을 생성하고 해당 내용을 프론ㅌ
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		//인증에 성공한 email 받아오기
		String userEmail = authResult.getName();

		//토큰 생성 - access 토큰 유효기간 30분
		String accessToken = jwtUtil.createJwt("access", userEmail, 30 * 60 * 1000L);
		response.addHeader("Authorization", "Bearer " + accessToken);// 헤더에 access 토큰 넣기
		//API 응답 생성
		createAPIResponse(response, REQUEST_OK);
	}

	//로그인 실패시 작동 - 실패시 json 응답을 작성해 보내는것이 목적
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {

		//API 응답 생성 - failed 예외는 BadCredentialsException 하나로만 처리함
		createAPIResponse(response, INCORRECT_AUTH_INFO);
	}

	private void createAPIResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		ApiResponse apiResponse = new ApiResponse<>(errorCode);
		response.setStatus(errorCode.getStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getWriter(), apiResponse);
	}

	// JSON 파일을 읽고 반환하는 역할
	private Map<String, String> getLoginInfoFromJson(HttpServletRequest request) {
		if (!"application/json".equals(request.getContentType())) {
			throw ModongException.from(ErrorCode.INVALID_REQUEST);
		}
		StringBuilder jsonString = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
		} catch (IOException e) {
			throw ModongException.from(ErrorCode.INVALID_REQUEST, "JSON 파일 읽기 실패");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonString.toString(), new TypeReference<Map<String, String>>() {
			});
		} catch (JsonProcessingException e) {
			throw ModongException.from(ErrorCode.INVALID_REQUEST, "JSON 형식에 맞지 않는 포맷");
		}
	}

}

