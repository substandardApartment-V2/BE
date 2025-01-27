package com.myapt.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*") // 허용할 도메인 -> 향후 변경 필요
			.allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드 설정 -> 향후 변경 필요
			.allowedHeaders("Origin", "Content-Type", "Accept") // 허용할 헤더 설정
			.maxAge(3600); // preflight 요청의 유효시간 설정
	}
}
