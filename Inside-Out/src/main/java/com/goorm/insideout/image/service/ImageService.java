package com.goorm.insideout.image.service;

import static com.goorm.insideout.global.exception.ErrorCode.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubPost;
import com.goorm.insideout.club.repository.ClubPostRepository;
import com.goorm.insideout.club.repository.ClubRepository;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.image.domain.ClubImage;
import com.goorm.insideout.image.domain.ClubPostImage;
import com.goorm.insideout.image.domain.MeetingImage;
import com.goorm.insideout.image.domain.ProfileImage;
import com.goorm.insideout.image.dto.StoreImageDto;
import com.goorm.insideout.image.repository.ClubImageRepository;
import com.goorm.insideout.image.repository.ClubPostImageRepository;
import com.goorm.insideout.image.repository.MeetingImageRepository;
import com.goorm.insideout.image.repository.ProfileImageRepository;
import com.goorm.insideout.image.repository.ImageStoreProcessor;
import com.goorm.insideout.meeting.domain.Meeting;
import com.goorm.insideout.meeting.repository.MeetingRepository;
import com.goorm.insideout.user.domain.User;
import com.goorm.insideout.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

	private final ClubImageRepository clubImageRepository;
	private final ClubPostImageRepository clubPostImageRepository;
	private final MeetingImageRepository meetingImageRepository;
	private final ProfileImageRepository profileImageRepository;
	private final ClubRepository clubRepository;
	private final ClubPostRepository clubPostRepository;
	private final MeetingRepository meetingRepository;
	private final UserRepository userRepository;
	private final ImageStoreProcessor imageStoreProcessor;
	private final AmazonS3 amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Transactional
	public void saveClubImages(List<MultipartFile> imageFiles, Long clubId) {
		Club club = clubRepository.findById(clubId)
			.orElseThrow(() -> ModongException.from(ErrorCode.CLUB_NOT_FOUND));

		List<StoreImageDto> storeImageDtos = imageStoreProcessor.storeImageFiles(imageFiles);

		for (int i = 0; i < imageFiles.size(); i++) {
			String imageUrl = uploadImageFile(imageFiles.get(i), storeImageDtos.get(i));

			ClubImage clubImage = storeImageDtos.get(i).toClubImageEntity(club, imageUrl);
			clubImageRepository.save(clubImage);
		}
	}

	@Transactional
	public void saveClubPostImages(List<MultipartFile> imageFiles, Long clubPostId) {
		ClubPost clubPost = clubPostRepository.findById(clubPostId)
			.orElseThrow(() -> ModongException.from(CLUB_POST_NOT_FOUND));

		List<StoreImageDto> storeImageDtos = imageStoreProcessor.storeImageFiles(imageFiles);

		for (int i = 0; i < imageFiles.size(); i++) {
			String imageUrl = uploadImageFile(imageFiles.get(i), storeImageDtos.get(i));

			ClubPostImage clubPostImage = storeImageDtos.get(i).toClubPostImageEntity(clubPost, imageUrl);
			clubPostImageRepository.save(clubPostImage);
		}
	}

	@Transactional
	public void saveMeetingImages(List<MultipartFile> imageFiles, Long meetingId) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));

		List<StoreImageDto> storeImageDtos = imageStoreProcessor.storeImageFiles(imageFiles);

		for (int i = 0; i < imageFiles.size(); i++) {
			String imageUrl = uploadImageFile(imageFiles.get(i), storeImageDtos.get(i));

			MeetingImage meetingImage = storeImageDtos.get(i).toMeetingImageEntity(meeting, imageUrl);
			meetingImageRepository.save(meetingImage);
		}
	}

	@Transactional
	public void saveProfileImage(List<MultipartFile> imageFiles, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		List<StoreImageDto> storeImageDtos = imageStoreProcessor.storeImageFiles(imageFiles);

		for (int i = 0; i < imageFiles.size(); i++) {
			String imageUrl = uploadImageFile(imageFiles.get(i), storeImageDtos.get(i));

			ProfileImage profileImage = storeImageDtos.get(i).toProfileImageEntity(user, imageUrl);
			user.setProfileImage(profileImage);
			userRepository.save(user);
			profileImageRepository.save(profileImage);
		}
	}

	// 만들긴 했는데 필요없을 것 같은 느낌이 강하게 들어서 일단 주석처리
	// public List<ImageResponse> findMeetingImages(Long meetingId) {
	// 	List<MeetingImage> meetingImages = meetingImageRepository.findByMeetingId(meetingId);
	//
	// 	return meetingImages.stream()
	// 		.map(meetingImage -> ImageResponse.from(meetingImage.getImage()))
	// 		.toList();
	// }
	//
	// public ImageResponse findProfileImage(Long userId) {
	// 	User user = userRepository.findById(userId)
	// 		.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));
	// 	Image profileImage = user.getProfileImage().getImage();
	//
	// 	return ImageResponse.from(profileImage);
	// }

	@Transactional
	public void deleteClubImages(Long clubId) {
		List<ClubImage> clubImages = clubImageRepository.findByClubId(clubId);

		clubImages.forEach(clubImage -> {
			clubImageRepository.delete(clubImage);

			String storeName = clubImage.getImage().getStoreName();
			amazonS3Client.deleteObject(bucket, storeName);
		});
	}

	@Transactional
	public void deleteClubPostImages(Long clubPostId) {
		List<ClubPostImage> clubPostImages = clubPostImageRepository.findByClubPostId(clubPostId);

		clubPostImages.forEach(clubPostImage -> {
			clubPostImageRepository.delete(clubPostImage);

			String storeName = clubPostImage.getImage().getStoreName();
			amazonS3Client.deleteObject(bucket, storeName);
		});
	}

	@Transactional
	public void deleteMeetingImages(Long meetingId) {
		List<MeetingImage> meetingImages = meetingImageRepository.findByMeetingId(meetingId);

		meetingImages.forEach(meetingImage -> {
			meetingImageRepository.delete(meetingImage);

			String storeName = meetingImage.getImage().getStoreName();
			amazonS3Client.deleteObject(bucket, storeName);
		});
	}

	@Transactional
	public void deleteProfileImages(Long userId) {
		Optional<ProfileImage> optionalProfileImage = profileImageRepository.findByUserId(userId);
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalProfileImage.isEmpty()) {
			throw ModongException.from(EMPTY_IMAGE);
		}
		if (optionalUser.isEmpty()) {
			throw ModongException.from(USER_NOT_FOUND);
		}
    
		User user = optionalUser.get();
		user.setProfileImage(null);
		userRepository.save(user);

		ProfileImage profileImage = optionalProfileImage.get();
		profileImageRepository.delete(profileImage);
		String storeName = profileImage.getImage().getStoreName();
		amazonS3Client.deleteObject(bucket, storeName);
	}

	private String uploadImageFile(MultipartFile multipartFile, StoreImageDto storeImageDto) {
		String storeName = storeImageDto.storeName();
		try {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(multipartFile.getContentType());
			objectMetadata.setContentLength(multipartFile.getSize());
			objectMetadata.setContentDisposition("inline");

			PutObjectRequest putObjectRequest = new PutObjectRequest(
				bucket,
				storeName,
				multipartFile.getInputStream(),
				objectMetadata
			);

			amazonS3Client.putObject(putObjectRequest);

			return amazonS3Client.getUrl(bucket, storeName).toString();
		} catch (IOException e) {
			throw ModongException.from(ErrorCode.S3_UPLOAD_FAILURE);
		}
	}
}
