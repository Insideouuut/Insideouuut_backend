package com.goorm.insideout.club.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goorm.insideout.club.entity.ClubQuestionAnswer;

public interface ClubQuestionAnswerRepository extends JpaRepository<ClubQuestionAnswer, Long> {

	@Query(value = "select * from club_question_answer " +
		"where apply_id = :applyId "
		,nativeQuery = true)
	List<ClubQuestionAnswer> findByClubApplyId(@Param("applyId")Long applyId);
}
