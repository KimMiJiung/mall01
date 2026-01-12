package com.sbproject.mall01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sbproject.mall01.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	// cart_item_id별 카트항목 조회 (회원별 카트항목 조회)
	public String SELECT_CART_ITEM_ID = 
			"select * from cart_item where cart_item_id = :cartItemId";
	@Query(value = SELECT_CART_ITEM_ID, nativeQuery = true)
	Page<CartItem> querySelectCartItemId(@Param("cartItemId") String cartItemId, Pageable pageable);
}
