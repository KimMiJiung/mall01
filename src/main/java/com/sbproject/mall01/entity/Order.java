package com.sbproject.mall01.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
 * < 주문 클래스 >
 * - 주문아이디(orderId), 고객(엔터티, Customer), 배송(엔터티, Shipping)
 * - 주문아이템(도서, Map<String, OrderItem>), 총주문금액(orderTotalPrice)
 * - 주의: order은 테이블명으로 사용 불가
 * - 해결책: 엔터티명은 order로 사용하고, 테이블명은 orders로 생성하도록 함.
 */
@RequiredArgsConstructor
@Data
@Entity(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	/*
	 * 주문과 고객과의 관계 -> 1 대 1의 관계
	 * - 주문을 삭제하면 고객에 대한 정보도 함께 삭제되도록 설정
	 * - 조인 컬럼은 customer_id로 연결
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	@Valid
	private Customer customer;
	
	/*
	 * 주문과 배송정보와 관계 -> 1 대 1의 관계
	 * - 주문을 삭제하면 배송정보도 함께 삭제되도록 설정
	 * - 조인 컬럼은 shipping_id로 연결
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shipping_id")
	@Valid
	private Shipping shipping;
	
	/*
	 * 주문과 주문항목과의 관계 -> 1 대 다의 관계
	 * - 주문 1개에는 여러 개의 주문아이템이 매핑될 수 있음
	 * - 주문을 삭제하면 연겨러된 주문아이템이 모두 삭제되도록 설정
	 * - 주문항목 아이디와 주문항목을 Map에 담아서 저장
	 * 
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_item_id")
	private Map<String, OrderItem> orderItems = new HashMap<>();
	
	// 총주문금액
	private int orderTotalPrice = 0;
	

	
	
}
