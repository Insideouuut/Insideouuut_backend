package com.goorm.insideout.auth.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {
	private final SecretKey secretKey;

	public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
		//secretKey 주입
		secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
			Jwts.SIG.HS256.key().build().getAlgorithm());
	}

	//claim 에서 email 정보 추출
	public String getUserEmail(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("userEmail", String.class);
	}

	//claim 에서 토큰 종류 정보 추출
	public String getCategory(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get("category", String.class);
	}

	//claim 에서 토큰 만료 여부 추출
	public Boolean isExpired(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getExpiration()
			.before(new Date());
	}

	//JWT 토큰 생성 Claim 에는 토큰 종류와 email 만 담음
	public String createJwt(String category, String userEmail, Long expiredMs) {

		return Jwts.builder()
			.claim("category", category)
			.claim("userEmail", userEmail)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiredMs))
			.signWith(secretKey)
			.compact();
	}

	// 토큰 검증 로직
	public boolean validateToken(String token) {
		try {
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
