package com.goorm.insideout.club.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goorm.insideout.club.entity.ClubApply;
import com.goorm.insideout.club.entity.ClubUser;

import jakarta.transaction.Transactional;

public interface ClubApplyRepository extends JpaRepository<ClubApply, Long> {
	@Transactional
	void deleteByUserIdAndClubId(Long userId, Long clubId);

	@Transactional
	void deleteByApplyId(Long applyId);

	Optional<ClubApply> findByApplyId(Long applyId);

	Optional<ClubApply> findByUserIdAndClubId(Long userId, Long clubId);

	@Query(value = "select * from club_apply " +
		"where club_id = :club_id "
		,nativeQuery = true)
	List<ClubApply> findByClubIdJQL(@Param("club_id") Long clubId);

	@Query(value = "select * from club_apply " +
		"where user_id = :user_id "
		,nativeQuery = true)
	List<ClubApply> findByUserIdJQL(@Param("user_id") Long userId);
}
