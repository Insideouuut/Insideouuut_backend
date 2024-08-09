package com.goorm.insideout.auth.filter;

import java.io.IOException;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.goorm.insideout.auth.domain.RefreshToken;
import com.goorm.insideout.auth.dto.CustomOAuth2User;
import com.goorm.insideout.auth.repository.RefreshTokenRepository;
import com.goorm.insideout.auth.utils.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class CustomOAuthLoginHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JWTUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;

	public CustomOAuthLoginHandler(JWTUtil jwtUtil,RefreshTokenRepository refreshTokenRepository) {
		this.jwtUtil = jwtUtil;
		this.refreshTokenRepository=refreshTokenRepository;
	}

	// 소셜 로그인 성공시 해당 메서드 실행
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal(); //인증 완료된 사용자 꺼내옴
		//해당 이메일로 refresh 토큰 생성
		String userEmail = customUserDetails.getEmail();
		String refresh = jwtUtil.createJwt("refresh", userEmail, 24 * 60 * 60 * 1000L);
		//쿠키에 담아 전송
		response.addHeader("Set-Cookie", createCookie("refresh", refresh).toString());
		//Redis 에 해당 토큰 저장
		RefreshToken refreshToken = new RefreshToken(refresh,userEmail);
		refreshTokenRepository.save(refreshToken);
		// reissue 요청으로 리다이렉트하기
		//response.sendRedirect("http://localhost:3000/?action=reissue");
		// response.sendRedirect("http://localhost:5173/reissue");
		response.sendRedirect("https://modong.link/reissue");
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
