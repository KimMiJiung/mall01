package com.sbproject.mall01.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// 주소 클래스의 맴버 변수
// 아이디, 국가, 우편번호, 기본주소, 상세주소

@Data
@Entity
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "국가명은 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(50)")
	private String country;
	
	@NotBlank(message = "우편번호는 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(10)")
	private String zipcode;
	
	@NotBlank(message = "기본주소는 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(100)")
	private String basicAddress;
	
	@NotBlank(message = "상세주소는 필수 입력 사항입니다.")
	@Column(columnDefinition = "varchar(100)")
	private String detailAddress;
}
