package com.goorm.insideout.auth.domain;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;
import lombok.Getter;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 24*60 * 60 * 1000L) // 1일로 유효기간 설정
public class RefreshToken {

	@Id
	private String refreshToken;
	private String userEmail;
	//redis 저장소의 key 값으로는 {value}:{@Id 어노테이션을 붙여준 값이 됨}
	//예를 들어 여기서는 refreshToken 에 ABC 라는 값을 넣는다면 "refreshToken":"ABC" 가된다

	public RefreshToken(String refreshToken, String userEmail) {
		this.refreshToken = refreshToken;
		this.userEmail = userEmail;
	}
}
