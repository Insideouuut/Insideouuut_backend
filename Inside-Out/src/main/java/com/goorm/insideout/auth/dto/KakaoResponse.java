package com.goorm.insideout.auth.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{

	private final Map<String, Object> attribute;
	private  final Map<String, Object> kakaoAccountAttribute;
	private final Map<String, Object> profileAttribute;

	public KakaoResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
		this.kakaoAccountAttribute = (Map<String, Object>) attribute.get("kakao_account");
		this.profileAttribute = (Map<String, Object>) kakaoAccountAttribute.get("profile");
	}
	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getEmail() {
		return kakaoAccountAttribute.get("email").toString();
	}

	@Override
	public String getName() {
		return profileAttribute.get("nickname").toString();
	}
}
