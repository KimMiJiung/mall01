package com.sbproject.mall01.entity;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

// 상품 클래스
/*
< 도서 클래스의 멤버 변수 (총 12개) > 
- id(PK), 도서 ID, 도서제목, 도서가격, 저자, 설명, 출판사, 분류(전문도서, 교육도서, 교양도서), 
- 재고수, 출판일, 도서종류(신규,중고,전자), 도서이미지
- ★ 주의) 쇼핑몰의 주체가 되는 엔터티 클래스의 멤버변수는 10개 정도로 정할 것 
- condition -> bookCondition 이름을 변경함.
- ★ Validation으로 설정한 어노테이션도 Entity로 생성한 테이블에 제약조건으로 추가됨.
*/

@Data
@ToString(exclude = {"description"}) // toString()에서 해당 필드는 제외하고 출력
@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 도서 ID는 ISBN 다음에 0~9 사이의 숫자로 시작
	@Pattern(regexp="ISBN[0-9]+")
	@NotBlank(message = "도서 ID는 필수 입력 사항입니다.")
	@Column(unique= true, columnDefinition="varchar(30)")
	private String bookId;
	
	// 도서명은 2~50 글자 사이
	@Size(min=2, max=50)
	@NotBlank(message = "도서명은 필수 입력 사항입니다.")
	private String title;
	
	// 최소값 0, 음수는 사용불가, 전체 8자리, 소수점 2자리
	@NotNull @Min(value=0) @Digits(integer=8, fraction=2)
	private Integer price;
	
	@NotBlank(message = "저자는 필수 입력 사항입니다.")		
	private String author;
	
	@NotBlank(message = "설명은 필수 입력 사항입니다.")	
	@Column(columnDefinition="text")
	private String description;
	
	@NotBlank(message = "출판사는  필수 입력 사항입니다.")	
	@Column(columnDefinition="varchar(100)")
	private String publisher;
	
	@Column(columnDefinition="varchar(30) default '컴퓨터/IT'")
	private String category; // default값 설정 : 컴퓨터/IT
	
	// 최소값 0, 음수는 사용 불가
	@NotNull @Min(value=0)
	private Integer stock;
	
	@NotBlank(message = "출판일은 반드시 선택해야합니다.")
	@Column(columnDefinition="char(10)")
	private String releaseDate; // 출판일은 고정 문자열로 입력, ex) 2025/10/17
	
	@Column(columnDefinition="varchar(10) default '신규'")
	private String bookCondition; // 신규, 중고, 전자책

	@Column(columnDefinition="varchar(30) default 'no_image.jpg'")
	private String fileName;  // 도서이미지의 파일명
	
	// 생성할 테이블에서는 필드는 제외함
	@Transient
	private MultipartFile bookImage; //업로드된 도서이미지
	
}
