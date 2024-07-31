package com.goorm.insideout.auth.service;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.goorm.insideout.auth.dto.CustomUserDetails;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> userData = userRepository.findByEmail(email);

		if (userData.isEmpty()) {
			throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + email);
			//UserDetails에 담아서 return하면 AutneticationManager가 검증 함
		}

		return new CustomUserDetails(userData.get());
	}
}
