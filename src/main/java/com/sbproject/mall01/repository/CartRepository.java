package com.sbproject.mall01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sbproject.mall01.entity.Cart;


@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

	// 장바구니 조회 (회원 ID별로 조회) -> 페이징 처리를 하지 않음
	Cart findByMemberId(String memberId);
	
	// 장바구니 조회 (회원 ID별로 조회) -> 페이징 처리를 함
	// - 수정이 필요
	//Page<Cart> findByMemberId(String memberId, Pageable pageable);	
	
	// 장바구니 삭제 (회원ID별로 전체 장바구니 삭제)
	//@Transactional
	//void deleteByMemberId(String memberId);
	
	@Transactional
	void deleteByMemberId(String memberId);
	
	// 장바구니 삭제 -> cartId와 bookId를 통해 회원별 카트의 해당 상품만 삭제
	public final String DELETE_CART_ITEM = "delete from cart_item where cart_item_id = :cartId and cart_items_key = :bookId";
	@Transactional @Modifying
	@Query(value = DELETE_CART_ITEM, nativeQuery = true)
	void queryDeleteCartItem(@Param("bookId") String bookId, @Param("cartId") String cartId);
	
	// 장바구니 갱신
	public final String UPDATE_QUANTITY = 
	"update cart_item set quantity = :quantity, all_price = :price * :quantity where cart_items_key = :bookId and cart_item_id = :cartId";
	@Transactional @Modifying
	@Query(value = UPDATE_QUANTITY, nativeQuery = true)
	void queryUpdateQuantity(@Param("bookId") String bookId, @Param("cartId") String cartId, @Param("quantity") int quantity, @Param("price") int price);
	
	// 장바구니 총액 수정 -> cartId를 사용하여 회원별 카트의 해당 상품의 수량을 수정할 때 총액을 수정
	// 장바구니 총액 수정 -> cartId를 사용하여 회원별 카트의 해당 상품만 삭제할 때 총액을 수정
	public final String UPDATE_TOTAL_PRICE = 
			"update cart set total_price = COALESCE((select sum(all_price) from cart_item where cart_item_id = :cartId),0) where cart_id = :cartId";
	@Transactional @Modifying
	@Query(value = UPDATE_TOTAL_PRICE, nativeQuery = true)
	void queryUpdateTotalPrice(@Param("cartId") String cartId);	
	
}
