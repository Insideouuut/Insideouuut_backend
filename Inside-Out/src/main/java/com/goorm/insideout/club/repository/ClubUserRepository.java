package com.goorm.insideout.club.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.goorm.insideout.club.entity.ClubUser;

import jakarta.transaction.Transactional;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser,Long> {

}
