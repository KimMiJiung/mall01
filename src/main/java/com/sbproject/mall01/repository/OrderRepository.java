package com.sbproject.mall01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbproject.mall01.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	// 회원ID별로 주문 조회 - 페이징 처리를 하지 않음
	//List<Order> findByCustomerCustomerIdOrderByOrderIdDesc(String customerId);
	
	Page<Order> findByCustomerCustomerIdOrderByOrderIdDesc(String customerId, Pageable pageable);
	
	// 회원ID별로 주문 조회 - 페이징 처리를 함
	Page<Order> findByCustomerCustomerIdAndShippingDateGreaterThanEqualOrderByOrderIdDesc(String customerId, String date,  Pageable pageable);
	
	Page<Order> findByCustomerCustomerIdAndShippingDateLessThanEqualOrderByOrderIdDesc(String customerId, String date,  Pageable pageable);
			
	// 회원ID별로 주문 조회 - 페이징 처리를 함
	Page<Order> findByCustomerCustomerIdAndShippingDateBetweenOrderByOrderIdDesc(String customerId, String date1, String date2, Pageable pageable);
	
	// orderId로 주문조회
	Order findByOrderId(Long orderId);
	
	
}
