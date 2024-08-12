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
import com.goorm.insideout.image.domain.ProfileImage;

import jakarta.transaction.Transactional;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser,Long> {
	@Transactional
	void deleteByUserIdAndClubId(Long userId, Long clubId);

	Optional<ClubUser> findByClubUserId(Long clubUserId);

	@Query("SELECT cu FROM ClubUser cu JOIN FETCH cu.user WHERE cu.userId = :userId AND cu.clubId = :clubId")
	Optional<ClubUser> findByUserIdAndClubId(Long userId, Long clubId);

	@Query(value = "select * from club_user " +
		"where club_id = :club_id "
		,nativeQuery = true)
	List<ClubUser> findByClubIdJQL(@Param("club_id") Long clubId);

	@Transactional
	Optional<ClubUser> findByUserId(Long userId);

	@Query("select (count(c) > 0) from ClubUser c where c.club.clubId = ?1")
	boolean clubUserExistByClubId(Long clubId);

}
