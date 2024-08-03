package com.goorm.insideout.report.service;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.report.domain.Report;
import com.goorm.insideout.report.dto.ReportRequest;
import com.goorm.insideout.report.repository.ReportRepository;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReportService {
	private final UserRepository userRepository;
	private final ReportRepository reportRepository;
	public void reportUser(User reportingUser,Long targetUserId, ReportRequest reportRequest){
		Optional<User> targetUser = userRepository.findById(targetUserId);
		if(targetUser.isEmpty()){
			throw ModongException.from(USER_NOT_FOUND);
		}
		if(reportingUser==null){
			throw ModongException.from(USER_NOT_FOUND);
		}

		targetUser.get().decreaseMannerTemperature();
		userRepository.save(targetUser.get());

		Report report = new Report(reportRequest.getReason(),reportingUser,targetUser.get());
		reportRepository.save(report);


	}

}
