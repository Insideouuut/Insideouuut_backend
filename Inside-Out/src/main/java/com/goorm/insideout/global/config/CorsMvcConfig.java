package com.goorm.insideout.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {

		corsRegistry.addMapping("/**")
			.allowedOrigins("http://localhost:5173","http://localhost:3000","https://modong-backend.site")
			.allowedMethods("GET", "POST", "PUT", "DELETE")
			.allowedHeaders("Authorization", "Content-Type")
			.exposedHeaders("Authorization")
			.allowCredentials(true);
	}
}
