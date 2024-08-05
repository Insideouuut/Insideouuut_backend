package com.goorm.insideout.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI api() {
		Server server = new Server();
		server.setUrl("https://modong-backend.site"); // https://에 접근 가능하게 설정

		return new OpenAPI()
			// 보안 관련
			.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
			.components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
			.info(apiInfo())
			.servers(List.of(server));
	}

	private Info apiInfo() {
		return new Info()
			.title("API TITLE")
			.description("DESCRIPTION")
			.version("0.0.1");
	}

	private SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP) // 스키마 유형 HTTP
			.bearerFormat("JWT") // 토큰 형식
			.scheme("bearer"); // 스키마 이름
	}

}