package com.goorm.insideout.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.goorm.insideout.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,String> {
	//JPA Repository 가 아닌 Crud Repository 를 상속받아야 한다
}
