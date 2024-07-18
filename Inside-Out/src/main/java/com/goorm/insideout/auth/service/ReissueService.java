package com.goorm.insideout.auth.service;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.goorm.insideout.auth.domain.RefreshToken;
import com.goorm.insideout.auth.repository.RefreshTokenRepository;
import com.goorm.insideout.auth.utils.JWTUtil;
import com.goorm.insideout.global.exception.ModongException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReissueService {
	private final JWTUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	public String createNewAccessToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = getRefreshTokenFromCookies(request); //쿠키에서 토큰 추출
		String userEmail = validateAndGetUserEmail(refreshToken); //추출한 토큰 검증 및 유저반환
		//토큰 생성
		String newAccess = jwtUtil.createJwt("access", userEmail, 30 * 60 * 1000L);
		String newRefresh = jwtUtil.createJwt("refresh", "fakeEmail", 24 * 60 * 60 * 1000L);
		//기존 리프레시 토큰 삭제
		refreshTokenRepository.deleteById(refreshToken);
		// 새로운 refresh 토큰 객체 생성 및 Redis 저장
		RefreshToken newRefreshToken = new RefreshToken(newRefresh, userEmail);
		refreshTokenRepository.save(newRefreshToken);
		// 새로운 refresh 토큰 쿠키에 삽입
		response.addHeader("Set-Cookie", createCookie("refresh", newRefresh).toString());

		return newAccess;
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

	private String validateAndGetUserEmail(String refreshToken) {
		//유효하지 않은 토큰 예외처리
		if (!jwtUtil.validateToken(refreshToken)) {
			throw ModongException.from(INVALID_REFRESH_TOKEN);
		}
		//refresh 토큰 만료시 예외처리
		if (jwtUtil.isExpired(refreshToken)) {
			throw ModongException.from(EXPIRED_REFRESH_TOKEN);
		}
		//페이로드에 refresh 토큰이 아니면 예외처리 (ex access token)
		String category = jwtUtil.getCategory(refreshToken);
		if (!category.equals("refresh")) {
			throw ModongException.from(INVALID_REFRESH_TOKEN);
		}
		//해당 토큰이 Redis에 저장되어 있지 않으면 예외처리
		Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findById(refreshToken);
		if (refreshTokenEntity.isEmpty()) {
			throw ModongException.from(INVALID_REFRESH_TOKEN);
		}
		//Redis 에 Value 값으로 저장되있던 email을 반환
		return refreshTokenEntity.get().getUserEmail();
	}

	private ResponseCookie createCookie(String key, String value) {
		return ResponseCookie.from(key, value)
			.path("/") //쿠키 경로 설정(=도메인 내 모든경로)
			.sameSite("None") //sameSite 설정 (크롬에서 사용하려면 해당 설정이 필요함)
			.httpOnly(false) //JS에서 쿠키 접근 가능하도록함
			.secure(true) // HTTPS 연결에서만 쿠키 사용 sameSite 설정시 필요
			.maxAge(24 * 60 * 60)// 쿠키 유효기간 설정 (=refresh 토큰 만료주기)
			.build();
	}

}
