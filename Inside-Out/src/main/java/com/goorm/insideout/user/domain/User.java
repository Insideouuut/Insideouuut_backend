package com.goorm.insideout.user.domain;

import java.time.LocalDate;
import java.util.Set;

import com.goorm.insideout.meeting.domain.Category;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "USERS")
@EqualsAndHashCode(callSuper = false, of = {"id"})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	private String password;

	@Column(nullable = false)
	private String name;

	private String profileImage;

	private String nickname;

	private String location;

	private LocalDate birthDate;

	private String phoneNumber;

	@ElementCollection(targetClass = Category.class, fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	private Set<Category> interests;

	@Enumerated(EnumType.STRING)
	private Gender gender;
}
