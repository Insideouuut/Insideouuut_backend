package com.goorm.insideout.club.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.goorm.insideout.club.entity.Club;

public interface ClubRepository extends JpaRepository<Club, Long> {

	List<Club> findAllByOrderByClubIdDesc();

	// 팀 아이디와 유저 아이디로 팀 조회(팀장 인지 확인)
	@Query(value = "select * from club " +
		"where club_id = :clubId " +
		"and owner_id = :userId "
		,nativeQuery = true)
	Optional<Club> findByClubIdAndUserIdJQL(@Param("clubId") Long clubId,
		@Param("userId") Long userId);

	@Query(value = "select * from club " +
		"where category = :category "
		,nativeQuery = true)
	List<Club> findByCategoryJQL(@Param("category") String category);
}
