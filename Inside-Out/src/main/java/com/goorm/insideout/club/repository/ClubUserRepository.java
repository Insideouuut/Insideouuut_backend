package com.goorm.insideout.club.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubUser;

import jakarta.transaction.Transactional;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser,Long> {
	@Transactional
	void deleteByUserIdAndClubId(Long userId, Long clubId);

	@Transactional
	void deleteByClubUserId(Long clubUserId);

	Optional<ClubUser> findByClubUserId(Long clubUserId);

	Optional<ClubUser> findByUserIdAndClubId(Long userId, Long clubId);

	@Query(value = "select * from club_user " +
		"where club_id = :club_id "
		,nativeQuery = true)
	List<ClubUser> findByClubIdJQL(@Param("club_id") Long clubId);

}
