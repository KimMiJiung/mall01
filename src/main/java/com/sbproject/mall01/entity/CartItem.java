package com.sbproject.mall01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
 * < 장바구니 항목 클래스 >
 * - 장바구니 담기는 각각의 항목
 * - 장바구니 항목 아이디, 도서(상품), 장바구니 항목 수량, 장바구니 총액 
 */
@RequiredArgsConstructor
@Data
@Entity
public class CartItem implements Comparable<CartItem>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/*
	 * CartItem : Book -> 다 대 1
	 * - 각 CartItem에는 Book이 1개씩, CartItem은 여러 개 
	 */
	@ManyToOne
	@JoinColumn(name = "book_num")
	private Book book;
	
	private int quantity = 1;
	
	private int allPrice = 0;
	
	public CartItem(Book book) {
		this.book = book;
		this.allPrice = book.getPrice();
	}
		
	public void setBook(Book book) {
		this.book = book;
		updateAllPrice();
	}	
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
		// 도서별 총액 = 도서가격 * 장바구니 수량
		updateAllPrice();
	}
	
	// 도서별 총액 = 도서가격 * 장바구니 수량
	public void updateAllPrice() {
		this.allPrice = this.book.getPrice() * this.quantity;
	}

	@Override
	public int compareTo(CartItem o) {
		// id에 대한 오름차순
		if (this.id > o.id) return 1;	
		else if (this.id < o.id) return -1;
		return 0;
	}
}
