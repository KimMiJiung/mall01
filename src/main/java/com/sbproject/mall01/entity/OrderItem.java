package com.sbproject.mall01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
 * < 주문항목 클래스 >
 * - 주문항목 아이디, 주문상품 아이디, 주문수량, 주문총가격
 * 
 */
@RequiredArgsConstructor
@Data
@Entity
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String bookId;
	
	private int quantity;
	
	private int allPrice = 0;
	
}
