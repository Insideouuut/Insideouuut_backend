package com.goorm.insideout.image.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.image.domain.MeetingImage;
import com.goorm.insideout.image.dto.StoreImageDto;
import com.goorm.insideout.image.dto.response.ImageResponse;
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

	private final MeetingImageRepository meetingImageRepository;
	private final ProfileImageRepository profileImageRepository;
	private final MeetingRepository meetingRepository;
	private final UserRepository userRepository;
	private final ImageStoreProcessor imageStoreProcessor;
	private final AmazonS3 amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Transactional
	public void saveMeetingImages(List<MultipartFile> imageFiles, Long meetingId) {
		Meeting meeting = meetingRepository.findById(meetingId)
			.orElseThrow(() -> ModongException.from(ErrorCode.MEETING_NOT_FOUND));

		List<StoreImageDto> storeImageDtos = imageStoreProcessor.storeImageFiles(imageFiles);

		storeImageDtos.stream()
			.map(storeImageDto -> storeImageDto.toMeetingImageEntity(meeting))
			.forEach(meetingImageRepository::save);

		uploadImageFile(imageFiles, storeImageDtos);
	}

	@Transactional
	public void saveProfileImage(List<MultipartFile> imageFiles, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> ModongException.from(ErrorCode.USER_NOT_FOUND));

		List<StoreImageDto> storeImageDtos = imageStoreProcessor.storeImageFiles(imageFiles);

		storeImageDtos.stream()
			.map(storeImageDto -> storeImageDto.toProfileImageEntity(user))
			.forEach(profileImageRepository::save);

		uploadImageFile(imageFiles, storeImageDtos);
	}

	public List<ImageResponse> findMeetingImages(Long meetingId) {
		List<MeetingImage> meetingImages = meetingImageRepository.findByMeetingId(meetingId);

		return meetingImages.stream()
			.map(meetingImage -> {
				String url = amazonS3Client.getUrl(bucket, meetingImage.getImage().getUploadName()).toString();
				return ImageResponse.from(meetingImage.getImage(), url);
			})
			.toList();
	}

	private void uploadImageFile(List<MultipartFile> multipartFiles, List<StoreImageDto> storeImageDtos) {
		for (int i = 0; i < multipartFiles.size(); i++) {
			MultipartFile multipartFile = multipartFiles.get(i);
			String storeName = storeImageDtos.get(i).storeName();

			try {
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(multipartFile.getContentType());
				objectMetadata.setContentLength(multipartFile.getSize());

				PutObjectRequest putObjectRequest = new PutObjectRequest(
					bucket,
					storeName,
					multipartFile.getInputStream(),
					objectMetadata
				);

				amazonS3Client.putObject(putObjectRequest);
			} catch (IOException e) {
				throw ModongException.from(ErrorCode.S3_UPLOAD_FAILURE);
			}
		}
	}
}
