package com.sbproject.mall01.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sbproject.mall01.entity.Order;
import com.sbproject.mall01.repository.CartRepository;
import com.sbproject.mall01.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	
	// 주문 저장 1 - 장바구니 전체 주문에 대한 저장
	// - orders 테이블에 전체데이터를 저장, cart 테이블에서 전체 데이터를 삭제 -> 트렌젝션 처리
	@Transactional
	@Modifying
	public void saveOrder(Order order) {
		this.orderRepository.save(order);
		this.cartRepository.deleteByMemberId(order.getCustomer().getCustomerId());
	}
	
	// 주문 저장 2 - 장바구니 1건 주문에 대한 주문 저장
	// - orders 테이블에 1건데이터를 저장, cart 테이블에서 1건 데이터를 삭제, cart 총액 수정 -> 트렌젝션 처리
	@Transactional
	@Modifying
	public void saveOrder(Order order, String cartId, String bookId) {
		this.orderRepository.save(order);
		this.cartRepository.queryDeleteCartItem(bookId, cartId);
		this.cartRepository.queryUpdateTotalPrice(cartId);	
	}
	
	// 주문 저장 3 - 상품 상세 보기에서 바로 주문 (장바구니 처리가 없음)
	public void saveOrderWithoutCart(Order order) {
		this.orderRepository.save(order);
	}
	
	// 회원ID별로 주문 조회 - 페이징 처리를 하지 않음
	/*
	public List<Order> getOrderListByCustomerId(String customerId) {
		return this.orderRepository.findByCustomerCustomerIdOrderByOrderIdDesc(customerId);
	}
	*/
	
	// 회원ID별로 주문 조회 - 페이징 처리를 함
	public Page<Order> getOrderListByCustomerIdAndShippingDate(String customerId, String searchDate1, String searchDate2, int pageNum, int pageSize, String sortField, String sortWay) {
		Pageable pageable = PageRequest.of(pageNum-1, pageSize, 
				sortWay.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

		if (searchDate1.isEmpty() && searchDate2.isEmpty()) {
			return this.orderRepository.findByCustomerCustomerIdOrderByOrderIdDesc(customerId,  pageable);
		} else if (!searchDate1.isEmpty() && !searchDate2.isEmpty()){
			return this.orderRepository.findByCustomerCustomerIdAndShippingDateBetweenOrderByOrderIdDesc(customerId, searchDate1, searchDate2, pageable);
		} else {
			if (!searchDate1.isEmpty()) {
				return this.orderRepository.findByCustomerCustomerIdAndShippingDateGreaterThanEqualOrderByOrderIdDesc(customerId, searchDate1, pageable);
			} else {
				return this.orderRepository.findByCustomerCustomerIdAndShippingDateLessThanEqualOrderByOrderIdDesc(customerId, searchDate2, pageable);
			}			
		}
	}
	
	// orderId로 주문 조회
	public Order getOrderByOrderId(Long orderId) {
		return this.orderRepository.findByOrderId(orderId);
	}
}
