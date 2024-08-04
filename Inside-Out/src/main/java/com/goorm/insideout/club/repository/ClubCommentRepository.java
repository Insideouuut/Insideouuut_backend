package com.goorm.insideout.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goorm.insideout.club.entity.ClubComment;

@Repository
public interface ClubCommentRepository extends JpaRepository<ClubComment, Long> {
}
