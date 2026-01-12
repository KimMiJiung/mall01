package com.sbproject.mall01.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sbproject.mall01.interceptor.MonitoringInterceptor;

// 로깅을 위한 환경설정 클래스
// 시스템을 운영할 때 주석을 풀고 로깅을 하도록 함
//@Configuration
public class LoggingConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MonitoringInterceptor());
	}
	
}
