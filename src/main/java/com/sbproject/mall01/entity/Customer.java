package com.sbproject.mall01.entity;

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
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
 * < 고객 클래스 >
 * - 고객 클래스 -> 회원 정보가 아닐 수도 있음.
 * - 고객 아이디( 회원아이디가 아님, 자동으로 생성되는 유일한 아이디), 고객아이디, 고객이름, 고객전화번호, 고객주소
 */
@RequiredArgsConstructor
@Data
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "varchar(30)")
	@NotBlank(message = "고객ID는 필수 입력사항입니다.")
	private String customerId;
	
	@Column(columnDefinition = "varchar(30)")
	@NotBlank(message = "고객명은 필수 입력사항입니다.")
	private String name;
	
	@Column(columnDefinition = "varchar(13)")
	@NotBlank(message = "전화번호는 필수 입력사항입니다.")
	@Pattern(regexp = "01(?:0|1|[6~9])[.-]?(\\d{3}|\\d{4})[.-](\\d{4})$", message = "전화번호 형식에 맞게 입력해주세요. ex) 010-1111-1111")	
	private String phone;
	
	/*
	 * Customer와 Address : 1 대 1의 관계
	 * - 고객은 1개의 주소를 가짐
	 * - 고객의 정보를 삭제하면 주소정보도 함께 삭제되도록 함.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	@Valid
	private Address address;
		
}
