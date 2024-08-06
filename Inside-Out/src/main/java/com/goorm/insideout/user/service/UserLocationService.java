package com.goorm.insideout.user.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.dto.request.LocationVerifiedRequest;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLocationService {
	private final UserRepository userRepository;

	public void insertUserLocation(LocationVerifiedRequest locationVerifiedRequest, User user){
		if(user==null){
			throw ModongException.from(ErrorCode.USER_NOT_FOUND);
		}
		Set<String> locations = new HashSet<>(locationVerifiedRequest.getLocations());
		user.setLocations(locations);
		user.setLocationVerified(locationVerifiedRequest.getIsVerified());
		userRepository.save(user);
	}
}
