package com.myapt.global.config;

import jakarta.validation.Validator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
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

	@Bean
	public Validator validator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource); // MessageSource 연결
		return bean;
	}

}
