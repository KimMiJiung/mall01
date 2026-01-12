package com.sbproject.mall01.entity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
 * < 장바구니 클래스 >
 * - 장바구니 전체 항목을 포함한 장바구니
 * - 카트 아이디, 장바구니 항목, 장바구니 총액
 */
@RequiredArgsConstructor
@Data
@Entity
public class Cart {

	// 카트 아이디는 세션으로 자동 생성 -> CartController
	@Id
	private String cartId;
	
	// 회원별로 장바구니 생성 - 널값은 불가
	@Column(nullable = false, columnDefinition = "varchar(30)")
	private String memberId;
	
	/*
	// < 장바구니 항목 >
	//  - 키: 카트 아이디(세션), 값: 카트 항목
	//  장바구니 : 장바구니 항목 -> 1 대 다의 관계
	// - 장바구니 1개에 장바구니 항목을 여러 개 매핑]
	// - 장바구니를 삭제하면, 담겨있는 장바구니 항목도 모두 삭제되어야 함.
	// < LinkedHashMap >
	// - 순서를 유지하는 맵
	*/
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cart_item_id")
	private Map<String, CartItem> cartItems = new HashMap<>(); 
	
	// 장바구니 저장 날짜
	private LocalDateTime cartDate;
	
	// 장바구니 전체 총액
	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private int totalPrice = 0;
	
	public Cart(String cartId, String memberId) {
		// 세션 아이디를 카트 아이디로 사용
		this.cartId = cartId;
		this.memberId = memberId;
		this.cartDate = LocalDateTime.now();
	}
	
	// 장바구니 등록 메서드
	public void addCartItem(CartItem item) {
		String bookId = item.getBook().getBookId();
		// 장바구니 항목에 이미 도서가 있다면 수량만 변경
		// 장바구니 항목에 도서가 없다면 새로 추가
		if (cartItems.containsKey(bookId)) {
			CartItem cartItem = cartItems.get(bookId);
			cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
			cartItems.put(bookId, cartItem);
		} else {
			cartItems.put(bookId, item);
		}
		updateTotalPrice();
	}
	
	// 장바구니 삭제 메서드 (개별) -> 일반 사용자(회원)
	public void removeCartItem(CartItem item) {
		cartItems.remove(item.getBook().getBookId());
		updateTotalPrice();
	}
	
	// 장바구니 삭제 메서드 (전체) -> 관리자
	
	// 총액 갱신 메서드
	public void updateTotalPrice() {
		totalPrice = 0;
		for (CartItem item : cartItems.values()) {
			this.totalPrice += item.getAllPrice();
		}
	}
}
