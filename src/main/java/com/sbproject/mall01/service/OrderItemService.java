package com.sbproject.mall01.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sbproject.mall01.entity.Order;
import com.sbproject.mall01.entity.OrderItem;
import com.sbproject.mall01.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderItemService {
	
	private final OrderItemRepository orderItemRepositoty;
	
	// order_item_id로 주문항목 조회 (Cart의 cart_id로 저장되어 있음)
	public List<OrderItem> findByOrderItemId(Long orderId) {
		
		return this.orderItemRepositoty.findByOrderItemId(orderId);
	}
	
	public int selectQuantity(Order order, String orderBookId) {
		int quantity = orderItemRepositoty.findByOrderItemQuantity(order.getOrderId(), orderBookId);
		return quantity;
	}
}
