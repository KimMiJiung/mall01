package com.sbproject.mall01.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sbproject.mall01.entity.Book;
import com.sbproject.mall01.entity.Cart;
import com.sbproject.mall01.entity.CartItem;
import com.sbproject.mall01.exception.BookIdException;
import com.sbproject.mall01.service.BookService;
import com.sbproject.mall01.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {

	private final CartService cartService;	
	private final BookService bookService;
		
	/*
	// 장바구니 목록 조회 (회원 ID별로 조회)  -> 페이징 처리를 하지 않음	
	@GetMapping("/member/{memberId}")
	public String requestCartByMemberId(@PathVariable("memberId") String memberId, Model model, HttpServletRequest request) throws Exception {
		Cart cart = cartService.getCartByMemberId(memberId);
		//List<CartItem> cartItem = new ArrayList<>();
		// 장바구니에 없을 때
		if (cart == null) {
			String sessionId = request.getSession(true).getId();
			cart = cartService.save(new Cart(sessionId, memberId));			
		}
		
		// < 장바구니에 상품을 추가할 때 최근 상품이 상위에 노출되도록 수정 >
		// Cart에서 cartItem id의 역순으로 추출하여 itemList에 저장 (id 역순 -> 최근 순)
		Map<String, CartItem> cartItemList = cart.getCartItems();
		//cartItemList.forEach((k,v) -> log.info(k + ":" + v.toString()));
		
		// id에 대한 역순(최근순)으로 정렬하여 저장하는 비교
		List<CartItem> itemList = new ArrayList<>();
		
		for (CartItem item : cartItemList.values()) {
			itemList.add(item);
		}
		
		Collections.sort(itemList.reversed());	// 내림차순으로 변경	
			
		model.addAttribute("cart", cart);
		model.addAttribute("itemList", itemList);
		return "cart/cart";
	}
	*/
	
	// 페이징 설정
	private int pageSize = 10;  // 페이지당 상품 개수 (4개씩 3줄)	
	private int pageBlock = 5;  // 페이지 블럭수 (보여지는 페이지 블럭이 5개씩)	
	
	// 장바구니 목록 조회 (회원 ID별로 조회) -> 페이징 처리를 함	
	@GetMapping("/member/{memberId}")
	public String requestCartByMemberId(@PathVariable("memberId") String memberId, 
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			Model model, HttpServletRequest request) throws Exception {
		Cart cart = cartService.getCartByMemberId(memberId);
		//List<CartItem> cartItem = new ArrayList<>();
		// 장바구니에 없을 때
		if (cart == null) {
			String sessionId = request.getSession(true).getId();
			cart = cartService.save(new Cart(sessionId, memberId));			
		}
		
		// 페이징 처리
		String cartId = cart.getCartId();
		String sortField = "id";
		String sortWay = "desc";
		Page<CartItem> paging = cartService.getCartItemByCartId(cartId, pageNum, pageSize, sortField, sortWay);
		List<CartItem> itemList = paging.getContent();
		
		// 페이지블럭의 시작번호, 끝번호 설정
		int startPage = pageNum - (pageNum -1) % pageBlock;
		int endPage = startPage + pageBlock - 1;
//		
		/*
		// < 장바구니에 상품을 추가할 때 최근 상품이 상위에 노출되도록 수정 >
		// Cart에서 cartItem id의 역순으로 추출하여 itemList에 저장 (id 역순 -> 최근 순)
		Map<String, CartItem> cartItemList = cart.getCartItems();
		//cartItemList.forEach((k,v) -> log.info(k + ":" + v.toString()));
		
		// id에 대한 역순(최근순)으로 정렬하여 저장하는 비교
		List<CartItem> itemList = new ArrayList<>();		
		for (CartItem item : cartItemList.values()) {
			itemList.add(item);
		}
		
		Collections.sort(itemList.reversed());	// 내림차순으로 변경	
		*/
		
		//List<CartItem> itemList = new ArrayList<>();		
//		for (CartItem item : cart.getCartItems().values()) {
//			itemList.add(item);
//		}
//		log.info("페이징 전: " + itemList.size());
		
		// < itemList를 페이징 처리해야 함 >
		// 장바구니의 페이징은 컨트롤러에서 시작과 끝이 이루어져야 함.
//		Sort sortWay = Sort.by("id").descending();
//		Pageable pageable = PageRequest.of(pageNum, pageSize);
//		int start = (int) pageable.getOffset();
//		int end = Math.min((start + pageable.getPageSize()), itemList.size() );
		
		// 페이지블럭의 시작번호, 끝번호 설정
//		int startPage = pageNum - (pageNum -1) % pageBlock;
//		int endPage = startPage + pageBlock - 1;
		
		//Page<CartItem> paging = new PageImpl<>(itemList, pageable, itemList.size());
		//Page<CartItem> paging = new PageImpl<>(itemList.subList(start, end), pageable, itemList.size());
//		itemList = paging.getContent();
		
		log.info("페이징: " + itemList);
		itemList.forEach(c -> log.info(c.toString()));
		log.info("페이징 후: " + itemList.size());
		
		// 페이징에서 사용한 값
		model.addAttribute("pageNum", pageNum);                      // 현재 페이지 번호
		model.addAttribute("pageBlock", pageBlock);                  // 페이지블럭수, ex) 5
		model.addAttribute("totalItems", paging.getTotalElements()); // 전체 건수, ex) 165건 
		model.addAttribute("totalPages", paging.getTotalPages());    // 전체 페이지수, ex) 165건 / 10씩 = 16.5 -> 17개 페이지
		model.addAttribute("startPage", startPage);                  // 블럭의 시작페이지
		model.addAttribute("endPage", endPage);                      // 블럭의 끝페이지		
		
		model.addAttribute("cart", cart);
		model.addAttribute("itemList", itemList);
		return "cart/cart";
	}		
	
	// 장바구니 추가, 갱신
	// RESTful 서비스에서 추가된 @PutMapping, @DeleteMapping을 사용할 때는 
	// @ResponseStatus(value = HttpStatus.NO_CONTENT)를 함께 사용해야 함.	
	@PutMapping("/book/{bookId}/{memberId}/{quantity}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void addCartNewItem(@PathVariable("bookId") String bookId, @PathVariable("memberId") String memberId, 
			@PathVariable("quantity") int quantity, HttpServletRequest request) {
		
		Cart cart = cartService.getCartByMemberId(memberId);
		log.info("cart : " + cart);
		
		// 장바구니에서 회원 ID가 없다면 -> 새로운 장바구니 생성 (카트ID를 세션으로 생성)
		if (cart == null) {
			String sessionId = request.getSession(true).getId();
			cart = cartService.save(new Cart(sessionId, memberId));
		}
		
		Book book = bookService.getBookByBookId(bookId);
		
		// 도서 정보가 없을 때 예외처리
		if (book == null) {
			throw new BookIdException(bookId);
		}
		
		log.info("memberId : " + memberId);
		log.info("bookId : " + bookId);
		log.info(book.toString());
				
		// 장바구니 목록에 도서를 추가
		CartItem cartItem = new CartItem(book);
		cartItem.setQuantity(quantity);
		cart.addCartItem(cartItem);
		
		// 장바구니 목록을 갱신
		cartService.save(cart);
	}
	
	// 장바구니 삭제 1 - 개별 장바구니 삭제
	// RESTful 서비스에서 추가된 @PutMapping, @DeleteMapping을 사용할 때는 
	// @ResponseStatus(value = HttpStatus.NO_CONTENT)를 함께 사용해야 함.
	@DeleteMapping("/book/{bookId}/{cartId}/{memberId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public String deleteCartItem(@PathVariable("bookId") String bookId, @PathVariable("cartId") String cartId,@PathVariable("memberId") String memberId, HttpServletRequest request) {
		cartService.deleteCartItem(bookId, cartId);	
		return "redirect:/cart/member/" + memberId;
	}

	// 장바구니 삭제 2 - 전체 장바구니  삭제
	// RESTful 서비스에서 추가된 @PutMapping, @DeleteMapping을 사용할 때는 
	// @ResponseStatus(value = HttpStatus.NO_CONTENT)를 함께 사용해야 함.	
	/*
	@DeleteMapping("/{memberId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public String deleteCartAll(@PathVariable("memberId") String memberId) {
		cartService.deleteCartAll(memberId);
		return "redirect:/cart/member/" + memberId;
	}
	*/
	
	
	// ★★★★★ 학생 스스로 해볼 것
	// 장바구니 삭제3 - 체크박스로 선택한 장바구니 삭제
	@DeleteMapping("/{memberId}/{cartId}/{bookId}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public String deleteCartCheckBox(@PathVariable("memberId") String memberId, @PathVariable("cartId") String cartId, @PathVariable("bookId") String bookId) {
		log.info("memberId" +  memberId);
		log.info("cartId" +  cartId);
		log.info("bookIdList" +  bookId);
		String[] bookIds = bookId.split(",");
		for (String bkId : bookIds) {
			cartService.deleteCartItem(bkId, cartId);	
		}

		return "redirect:/cart/member/" + memberId;
	}
	
	
	// 장바구니 개별 수량 수정
	@GetMapping("/book/{bookId}/{cartId}/{quantity}/{price}/{memberId}")
	public String updateCartQuantity(@PathVariable("bookId") String bookId,
			@PathVariable("cartId") String cartId,
			@PathVariable("quantity") int quantity, 
			@PathVariable("price") int price, 
			@PathVariable("memberId") String memberId,
			@RequestParam("pageNum") String pageNum) {
		cartService.updateCartQuantity(bookId, cartId, quantity, price);
				
		return "redirect:/cart/member/" + memberId + "?pageNum=" + pageNum;
	}
}
