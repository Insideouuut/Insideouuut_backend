package com.goorm.insideout.club.service;

import java.util.List;

import com.goorm.insideout.club.dto.ClubCommentDto;
import com.goorm.insideout.club.entity.ClubComment;

public class ClubCommentServiceImpl implements ClubCommentService{
	@Override
	public ClubComment save(ClubCommentDto clubCommentDto) {
		return null;
	}

	@Override
	public List<ClubComment> findAllComment(Long clubPostId) {
		return null;
	}

	@Override
	public void deleteComment(ClubCommentDto clubCommentDto) {

	}

	@Override
	public void deleteClubPostID(Long clubPostId) {

	}

	@Override
	public Integer countComment(Long clubPostId) {
		return null;
	}

	@Override
	public void updateComment(ClubCommentDto clubCommentDto) {

	}
}
