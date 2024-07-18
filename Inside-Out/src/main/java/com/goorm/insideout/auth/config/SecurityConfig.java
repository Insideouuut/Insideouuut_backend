package com.goorm.insideout.auth.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.goorm.insideout.auth.filter.JWTFilter;
import com.goorm.insideout.auth.filter.LoginFilter;
import com.goorm.insideout.auth.repository.RefreshTokenRepository;
import com.goorm.insideout.auth.utils.JWTUtil;
import com.goorm.insideout.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JWTUtil jwtUtil;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.cors((cors) -> cors
				.configurationSource(new CorsConfigurationSource() {
					//CORS 설정
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration configuration = new CorsConfiguration();
						configuration.setAllowedOrigins(List.of("https://localhost:3000"));
						configuration.setAllowedMethods(Collections.singletonList("*"));
						configuration.setAllowCredentials(true);
						configuration.setAllowedHeaders(Collections.singletonList("*"));
						configuration.setMaxAge(3600L);
						configuration.setExposedHeaders(List.of("Authorization"));
						return configuration;
					}
				}));

		http
			.csrf(AbstractHttpConfigurer::disable);

		http
			.formLogin(AbstractHttpConfigurer::disable);

		http
			.httpBasic(AbstractHttpConfigurer::disable);

		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/actuator/health", "/api/login", "/api/join", "/").permitAll()
				.anyRequest().authenticated());

		http
			//로그인 필터 전애 인가 정보 확인 필터 삽입
			.addFilterBefore(new JWTFilter(jwtUtil, userRepository), LoginFilter.class)
			//인증 필터 자리에 커스텀한 로그인 필터 삽입
			.addFilterAt(
				new LoginFilter(authenticationManager(authenticationConfiguration), refreshTokenRepository, jwtUtil,
					"/api/login"),
				UsernamePasswordAuthenticationFilter.class);
		//.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);
		//추후 로그아웃 필터 구현 예정

		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
