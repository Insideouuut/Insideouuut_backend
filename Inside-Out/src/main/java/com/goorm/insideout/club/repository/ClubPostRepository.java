package com.goorm.insideout.club.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.goorm.insideout.club.entity.Club;
import com.goorm.insideout.club.entity.ClubPost;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {

	@Query(value = "select * from club_post " +
		"where club_id = :clubId " +
		"and category = :category "
		,nativeQuery = true)
	List<ClubPost> findByClubIdAndTypeJQL(@Param("clubId") Long clubId,
		@Param("category") String category);

	@Query("select c from ClubPost c where c.club.clubId = ?1")
	List<ClubPost> findByClubId(Long clubId);

}
