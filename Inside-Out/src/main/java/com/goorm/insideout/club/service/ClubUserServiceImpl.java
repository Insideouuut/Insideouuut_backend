package com.goorm.insideout.club.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goorm.insideout.club.repository.ClubUserRepository;
import com.goorm.insideout.club.dto.ClubUserDto;
import com.goorm.insideout.club.entity.ClubUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ClubUserServiceImpl implements ClubUserService {

	private final ClubUserRepository clubUserRepository;

}
