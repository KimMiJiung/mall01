package com.sbproject.mall01.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sbproject.mall01.entity.Cart;
import com.sbproject.mall01.entity.CartItem;
import com.sbproject.mall01.repository.CartItemRepository;
import com.sbproject.mall01.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	
	
	// 회원ID별 카트 조회 -> 페이징처리를 하지 않음
	public Cart getCartByMemberId(String memberId) {
		return cartRepository.findByMemberId(memberId);
	}
	
	// 회원ID별 카트 조회 -> 페이징처리를 함
	
	public Page<CartItem> getCartItemByCartId(String cartId, int pageNum, int pageSize, String sortField, String sortWay) {
		Pageable pageable = PageRequest.of(pageNum-1, pageSize, 
				sortWay.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
		
		return cartItemRepository.querySelectCartItemId(cartId, pageable);
	}
	
	
	// 카트 저장
	public Cart save(Cart cart) {
		return cartRepository.save(cart);
	}
	
	// 회원ID별 전체 카트 삭제
	/*
	@Transactional
	public void deleteCartAll(String memberId) {
		cartRepository.deleteByMemberId(memberId);
	}
	*/
	@Transactional
	public void deleteCartAll(String bookId, String cartId) {
		cartRepository.queryDeleteCartItem(bookId, cartId);
	}	
	
	// 카트항목 Id와 도서 Id를 통해 회원의 장바구니에서 해당 도서만 삭제
	// 카트항목에서 삭제된 항목의 소계를 빼서 총액을 수정
	@Transactional
	@Modifying
	public void deleteCartItem(String bookId, String cartId) {
		cartRepository.queryDeleteCartItem(bookId, cartId);
		cartRepository.queryUpdateTotalPrice(cartId);
	}
	
	// 카트 항목 개별 수량 수정
	// 카트 항목 개별 수량 수정에 따른 카트 totalPrice를 수정
	@Transactional
	@Modifying	
	public void updateCartQuantity(String bookId, String cartId, int quantity, int price) {
		cartRepository.queryUpdateQuantity(bookId, cartId, quantity, price);
		cartRepository.queryUpdateTotalPrice(cartId);
	}
}
