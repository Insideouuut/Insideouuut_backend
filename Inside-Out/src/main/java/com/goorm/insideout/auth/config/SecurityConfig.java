package com.goorm.insideout.auth.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.goorm.insideout.auth.filter.CustomEntryPoint;
import com.goorm.insideout.auth.filter.CustomLogoutFilter;
import com.goorm.insideout.auth.filter.CustomOAuthLoginHandler;
import com.goorm.insideout.auth.filter.JWTFilter;
import com.goorm.insideout.auth.filter.LoginFilter;
import com.goorm.insideout.auth.repository.RefreshTokenRepository;
import com.goorm.insideout.auth.service.CustomOAuth2UserService;
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
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOAuthLoginHandler customOAuthLoginHandler;
	private final CustomEntryPoint customEntryPoint;

	private static final String[] PUBLIC_URLS = {
		"/actuator/health",
		"/oauth2/**",
		"/api/login/**",
		"/api/join",
		"/api/reissue",
		"/api/clubs",
		"/api/clubs/{clubId}",
		"/api/check-email",
		"/api/check-nickname",
		"/v3/**",
		"/swagger-ui/**",
		"/"
	};


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
						configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173","http://localhost:3000","https://modong-backend.site"));

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
			.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
					.userService(customOAuth2UserService))
				.successHandler(customOAuthLoginHandler));
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers(PUBLIC_URLS).permitAll()
				.anyRequest().authenticated());

		http.exceptionHandling((exceptionHandling)-> exceptionHandling
			.authenticationEntryPoint(customEntryPoint));

		http
			//소셜 로그인시 무한 루프 문제 해결을 위해 인가 검증필터는 로그인 필터 이후에 삽입
			.addFilterAfter(new JWTFilter(jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class)
			//인증 필터 자리에 커스텀한 로그인 필터 삽입
			.addFilterAt(
				new LoginFilter(authenticationManager(authenticationConfiguration), refreshTokenRepository, jwtUtil,
					"/api/login"),
				UsernamePasswordAuthenticationFilter.class)
			//로그아웃 전에 커스텀한 로그아웃 필터 적용
			.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);

		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
