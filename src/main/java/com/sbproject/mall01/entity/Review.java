package com.sbproject.mall01.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

/*
 * < 리뷰(상품후기) 멤버변수 >
 * - 아이디(id), 제목(title), 내용(content), 등록날짜(createDate), 별표(star)
 * - 상품(bookId)
 * - 답글(reply) ->  1대 다 매핑 
 * - 작성자(writer) -> 다 대 1 매핑
 * - 답글(reply) ->  1대 다 매핑 
 * - 추천인(voter)  -> 다 대 다 매핑
 */
@Data
@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Length(min = 2, max = 200, message = "리뷰 제목은 2글자 이상 200글자 이하로 입력해 주세요")
	private String subject;
	
	@Column(columnDefinition = "text")
	@Length(min = 2, max = 2000, message = "리뷰 내용은 2글자 이상 2000글자 이하로 입력해 주세요" )
	private String content;
	
	@CreatedDate
	private LocalDate createDate;
	
	@Column(columnDefinition="varchar(30)")
	private String bookId;
	
	// 평점 (1 ~ 5)
	private Float rating;
	
	/*
	 * < Review(리뷰)와 Reply(답글)의 관계
	 * - Review(리뷰) : Reply(답글) -> 1 : N(다)의 관계
	 * - 1개의 리뷰에는 여러 개의 답글이 달릴 수 있음.
	 * - mappedBy : 연결된 Reply에 있는 멤버변수명
	 * - cascade: CascadeType.REMOVE는 리뷰를 삭제하면, 해당 리뷰와 관련된 답글도 삭제됨
	 */
	@OneToMany(mappedBy = "review", cascade=CascadeType.REMOVE)
	private List<Reply> replyList;	
	
	/*
	 * < Review(리뷰)와 Member(글쓴이)의 관계 >
	 * - Review(리뷰) : Member(글쓴이) -> N : 1의 관계
	 * - 글쓴이는 여러 개의 리뷰를 작성할 수 있음.
	 */
	@ManyToOne
	private Member writer;

	/*
	 * < Review(리뷰)와 Set<Member>(추천인)의 관계 >
	 * - Review(리뷰) : Set<Member> -> M(다) : N(다)의 관계
	 * - 1개의 리뷰를 여러 추천인이 추천할 수 있고, 1명의 추천인은 여러 리뷰를 추천할 수 있음.
	 * - 추천인은 각 리뷰에 1번의 추천만 가능하도록 함
	 * - Set: 중복을 허용하지 않음. 같은 추천인이 같은 리뷰에 딱 1번만 추천할 수 있음.
	 * - review_voter 테이블 생성: review_id, voter_id의 두 개 필드가 생성되고, 슈퍼키로 기본키가 됨.
	 */
	@ManyToMany
	private Set<Member> voter;
	
}
