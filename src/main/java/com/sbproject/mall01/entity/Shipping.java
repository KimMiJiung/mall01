package com.sbproject.mall01.entity;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
 * < 배송정보 클래스 >
 * - 배송아이디, 배송고객이름(회원명이 아닐수도 있음), 배송일, 배송주소(회원의 주소가 아닐수도 있음) 
 */
@RequiredArgsConstructor
@Data
@Entity
public class Shipping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "varchar(30)")
	@NotBlank(message = "배송고객명은 필수 입력사항입니다.")
	private String name;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotBlank(message = "배송일은 필수 입력사항입니다.")
	private String date;
	
	/*
	 * Shipping : Address -> 1 대 1의 관계
	 * - Shipping 엔터티는 Address 엔터티의 address_id를 외래키로 가짐
	 * - Shipping 정보가 삭제된다면, Address 정보도 함께 삭제되어야 함.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	@Valid
	private Address address;
	
}
