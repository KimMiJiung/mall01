package com.sbproject.mall01.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 이미지 업로드
// 파일 시스템에 저장된 이미지에 접근하는 환경 설정 클래스
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**")                // 웹요청에 포함되도록 하는 외부 연결 URI 경로 
				.addResourceLocations("file:///" + "e:/upload/") // 파일 위치
				.setCachePeriod(60*60*24*365);                   // 접근 파일 캐시시간, 365일
	}
}
