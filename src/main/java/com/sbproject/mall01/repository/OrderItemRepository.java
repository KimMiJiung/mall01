package com.sbproject.mall01.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sbproject.mall01.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

	// order_item_id로 주문항목 조회 (Cart의 cart_id로 저장되어 있음)
	public final String SELECT_ORDER_ITEM_ID = "select * from order_item where order_item_id = :orderId";
	@Query(value = SELECT_ORDER_ITEM_ID, nativeQuery = true)
	List<OrderItem> findByOrderItemId(@Param("orderId") Long orderId);
	
	public final String SELECT_QUANTITY = "select quantity from order_item where order_items_key = :orderBookId and order_item_id = :orderId";
	@Query(value = SELECT_QUANTITY, nativeQuery = true)
	int findByOrderItemQuantity(@Param("orderId") Long orderId, @Param("orderBookId") String orderBookId);
	
}
