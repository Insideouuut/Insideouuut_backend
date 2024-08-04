package com.goorm.insideout.club.service;

import java.util.List;

import com.goorm.insideout.club.dto.ClubCommentDto;
import com.goorm.insideout.club.entity.ClubComment;

public interface ClubCommentService {
	ClubComment save(ClubCommentDto clubCommentDto);

	List<ClubComment> findAllComment(Long clubPostId);

	void deleteComment(ClubCommentDto clubCommentDto);

	void deleteClubPostID(Long clubPostId);

	Integer countComment(Long clubPostId);

	void updateComment(ClubCommentDto clubCommentDto);
}
