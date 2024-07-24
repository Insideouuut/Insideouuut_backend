package com.goorm.insideout.club.dto.requestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.goorm.insideout.club.dto.ClubUserDto;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.user.domain.User;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClubRequestDto {

	private String clubName;

	private String category;

	//private LocalDateTime createdAt;

	private String content;

	private String date;

	private String region;
	private String question;


	private Integer memberLimit;
	Integer price;
	private Integer ageLimit;

	//   private MultipartFile clubImg;

	private String clubImgUrl;
}
