package com.goorm.insideout.club.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.goorm.insideout.club.entity.ClubComment;
import com.goorm.insideout.club.entity.ClubPost;

@Repository
public interface ClubCommentRepository extends JpaRepository<ClubComment, Long> {

	@Query(value = "select * from club_comment " +
		"where club_post_id = :clubPostId "
		,nativeQuery = true)
	List<ClubComment> findByClubPostIdJQL(@Param("clubPostId") Long clubPostId);
}
