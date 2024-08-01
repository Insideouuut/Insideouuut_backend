package com.goorm.insideout.meeting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.goorm.insideout.meeting.domain.MeetingPlace;

public interface PlaceRepository extends JpaRepository<MeetingPlace, Long> {
	@Query("select p from MeetingPlace p where p.name = ?1 and p.latitude = ?2 and p.longitude = ?3")
	Optional<MeetingPlace> findPlaceByNameAndLocation(String name, Double latitude, Double longitude);
}
