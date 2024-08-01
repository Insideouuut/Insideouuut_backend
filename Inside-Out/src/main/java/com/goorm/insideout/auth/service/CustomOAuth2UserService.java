package com.goorm.insideout.auth.service;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.goorm.insideout.auth.dto.CustomOAuth2User;
import com.goorm.insideout.auth.dto.KakaoResponse;
import com.goorm.insideout.auth.dto.OAuth2Response;
import com.goorm.insideout.auth.dto.OAuth2UserDTO;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;

	// OAuth2 인증 요청을 받아 사용자 정보를 불러오는 메서드
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 상위 클래스에 위임하여 사용자 정보를 불러옴
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info(oAuth2User.getAttributes().toString());

		// provider 식별
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;
		// provider에 따라 OAuth2Response의 구현체를 OAuth2Response에 저장 (현재는 카카오만 적용중)
		if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}
		// oAuth2Response 로 이메일 받아와서 존재하는 사용자인지 아닌지 판변
		Optional<User> existData = userRepository.findByEmail(oAuth2Response.getEmail());
		if (existData.isEmpty()) { //존재 하지 하지 않으면 해당 정보로 회원가입 진행
			User user = User.builder()
				.email(oAuth2Response.getEmail())
				.name(oAuth2Response.getName())
				.build();
			userRepository.save(user);
			// OAuth2User 로 인가된 정보 전달
			OAuth2UserDTO oAuth2UserDTO = new OAuth2UserDTO(oAuth2Response.getName(), oAuth2Response.getEmail());
			return new CustomOAuth2User(oAuth2UserDTO);
		} else { //존재하는 사용자면 사용자 이름만 갱신
			User user = existData.get();
			user.setName(oAuth2Response.getName());
			userRepository.save(user);
			// OAuth2User 인가된 정보 전달
			OAuth2UserDTO oAuth2UserDTO = new OAuth2UserDTO(oAuth2Response.getName(), oAuth2Response.getEmail());
			return new CustomOAuth2User(oAuth2UserDTO);
		}
	}
}
