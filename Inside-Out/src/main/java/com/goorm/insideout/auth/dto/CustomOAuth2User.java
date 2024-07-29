package com.goorm.insideout.auth.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
	private final OAuth2UserDTO oAuth2UserDTO;
	@Override
	public Map<String, Object> getAttributes() {
		return null; // getAttributes 사용안함 - 카카오 구현시 타 소셜로그인과 형식이 일치시키기 어려움
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null; // 관리자 기능이 없으므로 사용안함
	}

	@Override
	public String getName() {
		return oAuth2UserDTO.getName();
	}

	public String getEmail(){
		return oAuth2UserDTO.getEmail();
	}
}
