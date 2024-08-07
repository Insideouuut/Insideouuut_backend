package com.goorm.insideout.image.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.goorm.insideout.global.exception.ErrorCode;
import com.goorm.insideout.global.exception.ModongException;
import com.goorm.insideout.image.dto.StoreImageDto;

@Component
public class ImageStoreProcessor {

	private static final List<String> WHITE_IMAGE_EXTENSION = List.of("jpg", "jpeg", "png");
	private static final int MAX_FILE_SIZE = 10_000_000; // 10MB
	private static final String EXTENSION_FILE_CHARACTER = ".";

	public List<StoreImageDto> storeImageFiles(List<MultipartFile> imageFiles) {
		final List<StoreImageDto> storeImageDtos = new ArrayList<>();

		for (MultipartFile imageFile : imageFiles) {
			validateImageFileSize(imageFile.getSize());
			validateImageFileEmpty(imageFile);

			storeImageDtos.add(storeImageFile(imageFile));
		}

		return storeImageDtos;
	}

	private StoreImageDto storeImageFile(MultipartFile imageFile) {
		String originalImageFileName = imageFile.getOriginalFilename();
		String storeImageFileName = createStoreImageFileName(originalImageFileName); // 랜덤한 UUID를 통해 이미지 이름 설정

		return new StoreImageDto(originalImageFileName, storeImageFileName);
	}

	private String createStoreImageFileName(String originalFilename) {
		final String extension = extractExtension(originalFilename);
		validateImageFileExtension(extension);

		final String uuid = UUID.randomUUID().toString();

		return uuid + EXTENSION_FILE_CHARACTER + extension;
	}

	private String extractExtension(String originalFilename) {
		int position = originalFilename.lastIndexOf(EXTENSION_FILE_CHARACTER);

		return originalFilename.substring(position + 1);
	}

	private void validateImageFileSize(long imageSize) {
		if (imageSize > MAX_FILE_SIZE) {
			throw ModongException.from(ErrorCode.IMAGE_SIZE_EXCEEDED,
				"%s은/는 %d 이하여야 합니다.".formatted("이미지 파일 크기", MAX_FILE_SIZE));
		}
	}

	private void validateImageFileEmpty(MultipartFile imageFile) {
		if (imageFile.isEmpty()) {
			throw ModongException.from(ErrorCode.EMPTY_IMAGE);
		}
	}

	private void validateImageFileExtension(final String extension) {
		// 지원하지 않는 파일 확장자를 사용한 경우, 해당 확장자 정보와 함께 예외 반환
		if (!WHITE_IMAGE_EXTENSION.contains(extension)) {
			throw ModongException.from(
				ErrorCode.UNSUPPORTED_IMAGE_FILE_EXTENSION,
				ErrorCode.UNSUPPORTED_IMAGE_FILE_EXTENSION.getMessage() + " | " + extension
			);
		}
	}
}