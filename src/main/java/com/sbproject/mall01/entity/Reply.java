package com.sbproject.mall01.entity;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

/*
 * < 답글(Reply) 멤버 변수 >
 * - 아이디, 내용, 등록날짜
 * - 리뷰(review) - 다 대 1의 매핑 
 * - 작성자(writer) - 다 대 1의 매핑
 * - 추천인(Set<Member>) - 다 대 다의 매핑
 */
@Data
@Entity
public class Reply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "text")
	@Length(min = 5, max = 1000, message = "답글 내용은 5글자 이상 1000글자 이하로 입력해 주세요.")
	private String content;
	
	@CreatedDate
	private LocalDate createDate;
	
	/*
	 * < Reply와 Review의 관계 >
	 * Reply(답글) : Review(후기) -> N(다) : 1의 관계
	 * - 답글 여러개는 후기 하나를 참조함.
	 * - 답글은 후기를 참조해야 함.	 
	 * - Reply 엔터티: 자식 엔터티
	 * - Review 엔터티: 부모 엔터티
	 */	
	@ManyToOne
	private Review review;
	
	
	/*
	 * < Reply와 Member의 관계 >
	 * Reply(답글) : Member(글쓴이) -> N(다) : 1의 관계
	 * - 글쓴이는 여러 개의 답글을 작성할 수 있음.
	 */
	@ManyToOne
	private Member writer;
	
	/*
	 * < Reply와 Set<Member>의 관계 >
	 * Reply(답글) : Member(추천) -> M(다) : N(다)의 관계
	 * - 1개의 답글에는 여러 회원이 추천할 수 있고, 1명의 회원은 여러 답글을 추천할 수 있음
	 * - 회원의 각 답글에 1번의 추천만 가능함 -> Set을 사용
	 * - Set: 중복을 허용하지 않음, 같은 회원은 같은 답글에 2번의 추천을 할 수는 없음.
	 * - reply_voter 테이블 생성: reply_id, voter_id의 두 개 필드가 생성되고, 슈퍼키로 기본키가 됨. 
	 */
	@ManyToMany
	private Set<Member> voter;
	
}
