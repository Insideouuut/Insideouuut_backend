package com.goorm.insideout.club.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubUser;
import com.goorm.insideout.club.repository.custom.ClubQueryDslRepository;
import com.goorm.insideout.meeting.repository.custom.MeetingQueryDslRepository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long>, ClubQueryDslRepository {

	List<Club> findAllByOrderByClubIdDesc();

	// 참여 하고 있는 클럽이 있는 지 조회
	@Query(value = "select * from club " +
		"where id in " +
		"(select club_id from club_user where user_id = :userId) "
		,nativeQuery = true)
	Optional<Club> belongToTeam(@Param("userId") Long userId);

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
