package com.goorm.insideout.user.domain;

import java.math.BigDecimal;
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

	@Column(precision = 4, scale = 1)
	private BigDecimal mannerTemp;

	@ElementCollection(targetClass = Category.class, fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	private Set<Category> interests;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	public void increaseMannerTemperature(){
		BigDecimal temp = this.getMannerTemp().add(BigDecimal.valueOf(0.1));
		if(temp.compareTo(BigDecimal.valueOf(100)) > 0){
			this.mannerTemp=BigDecimal.valueOf(100);
		}
		else {
			this.mannerTemp = this.getMannerTemp().add(BigDecimal.valueOf(0.1));
		}
	}
	public void decreaseMannerTemperature(){
		BigDecimal temp = this.mannerTemp=this.getMannerTemp().subtract(BigDecimal.valueOf(5.0));
		if(temp.compareTo(BigDecimal.valueOf(0)) < 0){
			this.mannerTemp=BigDecimal.valueOf(0);
		}
		this.mannerTemp=this.getMannerTemp().subtract(BigDecimal.valueOf(5.0));
	}
}
