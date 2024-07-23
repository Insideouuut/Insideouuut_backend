package com.goorm.insideout.auth.filter;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goorm.insideout.auth.domain.RefreshToken;
import com.goorm.insideout.auth.repository.RefreshTokenRepository;
import com.goorm.insideout.auth.service.ReissueService;
import com.goorm.insideout.auth.utils.JWTUtil;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.global.response.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
	private final JWTUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		doFilter((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		IOException,
		ServletException {

		//요청 url이 "/api/logout" 이 아닌 경우 다음 필터로 넘김
		String requestUri = request.getRequestURI();
		if (!requestUri.matches("^\\/api/logout$")) {
			filterChain.doFilter(request, response);
			return;
		}
		// 요청 메서드가 POST 가 아닌 경우 다음 필터로 넘김
		String requestMethod = request.getMethod();
		if (!requestMethod.equals("POST")) {
			filterChain.doFilter(request, response);
			return;
		}
		//토큰 추출
		String refreshToken = getRefreshTokenFromCookies(request);
		//토큰 검증 실패시 다음 필터로 넘김
		if (!isTokenValid(refreshToken, response)) {
			return;
		}
		//Redis에 저장되어있는 토큰 삭제
		refreshTokenRepository.deleteById(refreshToken);

		//Refresh 토큰 Cookie 값 0
		Cookie cookie = new Cookie("refresh", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setHttpOnly(false);
		response.addCookie(cookie);

		//API 응답 생성 및 다음 필터로 넘기지 않음
		createAPIResponse(response,REQUEST_OK);
	}
	private String getRefreshTokenFromCookies(HttpServletRequest request) {
		// 쿠키에서 리프레쉬 토큰을 찾아옴
		String refresh = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("refresh")) {
				refresh = cookie.getValue();
			}
		}
		// 찾을 수 없으면 예외처리
		if (refresh == null) {
			throw ModongException.from(NOT_FOUND_REFRESH_TOKEN);
		}
		return refresh;
	}
	private boolean isTokenValid(String refreshToken, HttpServletResponse response) throws IOException {
		if (!jwtUtil.validateToken(refreshToken)) {
			createAPIResponse(response, INVALID_REFRESH_TOKEN);
			return false;
		}
		// refresh 토큰 만료 시 예외처리
		if (jwtUtil.isExpired(refreshToken)) {
			createAPIResponse(response, EXPIRED_REFRESH_TOKEN);
			return false;
		}
		// 페이로드에 refresh 토큰이 아니면 예외처리 (ex access token)
		String category = jwtUtil.getCategory(refreshToken);
		if (!category.equals("refresh")) {
			createAPIResponse(response, INVALID_REFRESH_TOKEN);
			return false;
		}
		// 해당 토큰이 Redis에 저장되어 있지 않으면 예외처리
		Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findById(refreshToken);
		if (refreshTokenEntity.isEmpty()) {
			createAPIResponse(response, INVALID_REFRESH_TOKEN);
			return false;
		}
		return true;
		// 위 상황 처럼 refresh token 에 문제가 있으면 로그아웃을 못하나요??
		// -> refresh 토큰에 문제가 있으면 access 토큰 재발급 자체가 안됨 (=이미 로그아웃된 상태)
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
