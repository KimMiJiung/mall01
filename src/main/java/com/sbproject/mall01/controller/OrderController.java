package com.sbproject.mall01.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sbproject.mall01.entity.Book;
import com.sbproject.mall01.entity.Cart;
import com.sbproject.mall01.entity.CartItem;
import com.sbproject.mall01.entity.Customer;
import com.sbproject.mall01.entity.Member;
import com.sbproject.mall01.entity.Order;
import com.sbproject.mall01.entity.OrderItem;
import com.sbproject.mall01.entity.Shipping;
import com.sbproject.mall01.exception.StockException;
import com.sbproject.mall01.service.BookService;
import com.sbproject.mall01.service.CartService;
import com.sbproject.mall01.service.MemberService;
import com.sbproject.mall01.service.OrderItemService;
import com.sbproject.mall01.service.OrderService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/order")
public class OrderController {

	private final OrderService orederService;
	private final MemberService memberService;
	private final CartService cartService;
	private final BookService bookService;	
	private final OrderItemService orderItemService;
	private final HttpSession session;
	private String[] orderIdArr = null;
	private Order order;
	private int currentStock = 0;

	// 주문과정: 주문 고객 입력 화면  -> 배송 정보 입력 화면 -> 주문 완료

	
	// 주문 등록 화면 1-1 (장바구니 전체 주문) - 주문 고객 입력 화면
	// - 고객ID만 필요
	// 주문 등록 화면 1-2 (장바구니 1건 주문) - 주문 고객 입력 화면	
	// -- 고객ID와 도서ID 모두 필요
	// 주문 등록 화면 1-3 (제품 상세에서 주문) - 주문 고객 입력 화면
	// -- 고객ID와 도서ID, 주문 수량 정보가 필요
	@GetMapping(value = {"/{memberId}", "/{memberId}/{bookId}","/{memberId}/{bookId}/{quantity}"})
	public String requestCustomerForm(@PathVariable("memberId") String memberId,
			@PathVariable(value="bookId", required=false) String bookId,
			@PathVariable(value="quantity", required=false) String quantity,
			Model model) {
		
		// 기존의 orderBookId 세션과 quantity 세션을 삭제
		session.removeAttribute("orderBookId");
		session.removeAttribute("orderBookIdList");
		session.removeAttribute("quantity");
		
		// bookId가 있을 때는 장바구니 1건 주문 -> bookId를 세션으로 저장 
		orderIdArr = bookId.split(",");
		
		if (orderIdArr.length == 1) {
			session.setAttribute("orderBookId", bookId);
			orderIdArr = null;
		}
		
		// quantity가 있을 때는 제품 상세에서 주문 -> quantity를 세션으로 저장
		if (quantity != null) {
			session.setAttribute("quantity", quantity);
		}
		
		// 고객(회원) 정보 획득
		Member member = memberService.findByMemberId(memberId);

		Customer customer = new Customer();
		customer.setCustomerId(member.getMemberId());
		customer.setName(member.getName());
		customer.setPhone(member.getPhone());		
		customer.setAddress(member.getAddress());

		// 주문 정보 생성
		order = new Order();

		model.addAttribute("customer", customer);
		return "order/orderCustomer";
	}
	
	// ★★★★★ 학생 스스로 해볼 것
	// 주문등록 화면 1-4 (장바구니에서 체크박스로 선택한 상품을 주문)
	/*
	public String requestCustomerForm2() {
		
	}
	*/
	
	// 주문 등록 처리 1 처리 -> 주문 고객 처리 -> 배송 정보 입력 화면으로 이동
	@PostMapping("/orderCustomer")
	public String requestCustomerPro(@Valid @ModelAttribute Customer customer, BindingResult bindingResult) {
		// 유효성 검증
		if (bindingResult.hasErrors()) {
			return "order/orderCustomer";
		}
		// 주문에 고객 정보 추가
		order.setCustomer(customer);

		return "redirect:/order/orderShipping";
	}

	// 주문 등록 화면 2
	@GetMapping("/orderShipping")
	public String requestShippingForm(Model model) {		
		// 배송 정보 생성
		Shipping shipping = new Shipping();
		shipping.setName(order.getCustomer().getName());
		shipping.setAddress(order.getCustomer().getAddress());

		// 주문일 기본값을 오늘 날짜로 생성하여 추가
		LocalDate now = LocalDate.now();
		shipping.setDate(now.toString());

		model.addAttribute(shipping);
		return "order/orderShipping";
	}

	// 주문 등록 처리 2 - 배송 처리 -> 주문 최종 확인 화면으로 이동
	@PostMapping("/orderShipping")
	public String requestShippingPro(@Valid @ModelAttribute Shipping shipping, BindingResult bindingResult, 
			Model model) {

		// 유효성 검증
		if (bindingResult.hasErrors()) {
			return "order/orderShipping";
		}

		// 주문에 배송 정보 추가
		order.setShipping(shipping);


		// 장바구니의 모든 장바구니항목 정보를 주문에 저장
		// 장바구니항목 중에서 상품(도서)정보는 bookList에 저장
		List<Book> bookList = new ArrayList<>();
		Cart cart = cartService.getCartByMemberId(order.getCustomer().getCustomerId());	
		List<String> seOrderBookIdList = new ArrayList<>();
		if (orderIdArr != null) {
			for (String orderBookId2 : orderIdArr) {			
				seOrderBookIdList.add(orderBookId2);
			}
		}

		session.setAttribute("orderBookIdList", seOrderBookIdList);
		ArrayList<String> orderBookIdList = (ArrayList<String>) session.getAttribute("orderBookIdList");		
		String orderBookId = (String)session.getAttribute("orderBookId");
		String quantity = (String) session.getAttribute("quantity");
		
		int allPriceSum = 0;
		if (orderBookIdList != null  && orderBookId == null) {
			// 장바구니 선택 주문			
			for (int i = 0; orderBookIdList.size() > i; i++) {
				for (CartItem cartItem : cart.getCartItems().values() ) {					
					if (cartItem.getBook().getBookId().equals(orderBookIdList.get(i))) {
						log.info(cartItem.getBook().getBookId());
						// bookList에 카트의 도서(상품)정보를 저장
						bookList.add(cartItem.getBook());
						// 카트항목을 주문항목에 추가
						OrderItem orderItem = new OrderItem();
						orderItem.setBookId(cartItem.getBook().getBookId());
						orderItem.setAllPrice(cartItem.getAllPrice());
						orderItem.setQuantity(cartItem.getQuantity());
						// 주문항목을 주문에 추가
						order.getOrderItems().put(orderItem.getBookId(), orderItem);
						// 1건일 때 : order.orderTotalPrice에 cartItem.allPrice를 추가
						allPriceSum += cartItem.getAllPrice();	
					}					
				}
			}
			order.setOrderTotalPrice(allPriceSum);
			// cartId를 세션으로 저장
			session.setAttribute("cartId", cart.getCartId());		
		} else if (quantity == null) {
			// 장바구니 1건 주문 - 세션으로 저장해둔 orderBookId와 memberId로 주문
			for (CartItem cartItem : cart.getCartItems().values() ) {
				if (cartItem.getBook().getBookId().equals(orderBookId)) {
					// bookList에 카트의 도서(상품)정보를 저장
					bookList.add(cartItem.getBook());
					// 카트항목을 주문항목에 추가
					OrderItem orderItem = new OrderItem();
					orderItem.setBookId(cartItem.getBook().getBookId());
					orderItem.setAllPrice(cartItem.getAllPrice());
					orderItem.setQuantity(cartItem.getQuantity());
					// 주문항목을 주문에 추가
					order.getOrderItems().put(orderItem.getBookId(), orderItem);
					// 1건일 때 : order.orderTotalPrice에 cartItem.allPrice를 추가
					order.setOrderTotalPrice(cartItem.getAllPrice());
				}
			}
			// cartId를 세션으로 저장
			session.setAttribute("cartId", cart.getCartId());
		} else {
			// 제품 상세에서 주문 - 세션으로 저장해둔 orderBookId와 quantity를 사용하여 주문
			
			// bookId를 통해서 book 정보 생성
			Book book = bookService.getBookByBookId(orderBookId);			
			// bookList에 book 정보 저장
			bookList.add(book);
			// 주문항목에 bookId 정보 저장
			OrderItem orderItem = new OrderItem();
			orderItem.setBookId(orderBookId);
			orderItem.setQuantity(Integer.parseInt(quantity));
			orderItem.setAllPrice(Integer.parseInt(quantity) * book.getPrice());
			// 주문항목을 주문에 추가 
			order.getOrderItems().put(orderItem.getBookId(), orderItem);
			order.setOrderTotalPrice(orderItem.getAllPrice());
			session.setAttribute("cartId", cart.getCartId());
		}

		model.addAttribute("bookList", bookList);
		model.addAttribute("order", order);

		return "order/orderConfirm";
	}

	// 주문 등록 처리 3 ->  주문 완료 (DB에 저장)
	// - order 객체에 저장된 데이터를 DB로 전송
	@PostMapping("/orderConfirm")
	public String requestOrderConfirm(Model model) {
		List<String> orderBookIdList = (List<String>) session.getAttribute("orderBookIdList");
		String orderBookId = (String) session.getAttribute("orderBookId");
		String quantity = (String) session.getAttribute("quantity");
		String cartId = (String) session.getAttribute("cartId");
		log.info("상품 선택 아이디:" + orderBookIdList);
		log.info("상품 아이디:" + orderBookId);
		log.info("주문 수량:" + quantity);
		log.info("카트아이디:" + cartId);
		currentStock = 0;	
		int orderQuantity = 0;
		if (orderBookIdList != null && orderBookId == null) {
			// 장바구니 전체 주문 -> orderBookId가 없을 때
			for (String orderBkId : orderBookIdList) {
				currentStock = 0;
				orderQuantity = 0;
				
				currentStock = this.bookService.getStock(orderBkId);
				this.orederService.saveOrder(order, cartId , orderBkId);
				
				orderQuantity = this.orderItemService.selectQuantity(order, orderBkId);
				if (currentStock > 0) {					
					this.bookService.updateStock(orderQuantity, orderBkId);
				} else {
					 throw new StockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.currentStock + ")");
				}			
			}			
		} else if(quantity == null) {
			currentStock = this.bookService.getStock(orderBookId);
			// 장바구니 1건 주문 -> orderBookId는 있고, quantity는 없을 때
			this.orederService.saveOrder(order, cartId , orderBookId);
			orderQuantity = this.orderItemService.selectQuantity(order, orderBookId);
			if (currentStock > 0) {				
				this.bookService.updateStock(orderQuantity, orderBookId);
			} else {
				 throw new StockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.currentStock + ")");
			}	
		} else {
			currentStock = this.bookService.getStock(orderBookId);
			// 상품 상세보기에서 바로 주문 -> orderBookId와 quantity가 모두 있을 대			
			if (currentStock > 0) {
				this.orederService.saveOrderWithoutCart(order);
				this.bookService.updateStock(Integer.valueOf(quantity), orderBookId);
			} else {
				 throw new StockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.currentStock + ")");
			}			
		}
		
		// 세션 삭제
		session.removeAttribute("orderBookIdList");
		session.removeAttribute("orderBookId");
		session.removeAttribute("cartId");
		session.removeAttribute("quantity");
		
		return "order/orderSuccess";
	}

	// 주문 취소
	@GetMapping("/orderCancel")
	public String requestOrderCancel() {
		// 세션 삭제
		session.removeAttribute("orderBookIdList");
		session.removeAttribute("orderBookId");
		session.removeAttribute("cartId");
		session.removeAttribute("quantity");
		
		return "order/orderCancel";
	}

	// 주문 전체조회 (회원ID별로 조회) - 회원ID가 고객ID로 사용됨 -> 페이징 처리하지 않음
	/*
	@GetMapping("/member/{memberId}")
	public String requestOrderListByCustomerId(@PathVariable("memberId") String memberId, Model model) {
		List<Order> orderList = this.orederService.getOrderListByCustomerId(memberId);

		// orderList에 대한 상품명(도서명) -> 주문 내역에 출력
		List<String> bookTitleList = new ArrayList<>();
		for (Order order : orderList) {
			for (OrderItem item : order.getOrderItems().values()) {
				Book book = bookService.getBookByBookId(item.getBookId());
				bookTitleList.add(book.getTitle());
				break;
			}
		}

		//log.info(bookTitleList.toString());

		model.addAttribute("bookTitleList", bookTitleList);
		model.addAttribute("orderList", orderList);
		return "order/orderList";
	}
	*/
	
	@PostMapping("/search/{memberId}")
	public String requestOrderListBySearch (@PathVariable("memberId") String memberId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "searchDate1") String searchDate1, 
			@RequestParam(name = "searchDate2") String searchDate2,
			Model model) {
		
		return requestOrderListByCustomerIdShippingDate(memberId, pageNum, searchDate1, searchDate2, model);		
	}
	
	
	// 페이징 설정
	private int pageSize = 10;  // 페이지당 상품 개수 (한 페이지에 10개씩)	
	private int pageBlock = 5;  // 페이지 블럭수 (보여지는 페이지 블럭이 5개씩)	
	
	// 주문 전체조회 (회원ID별로 조회) - 회원ID가 고객ID로 사용됨 -> 페이징 처리를 함
	@GetMapping("/member/{memberId}")
	public String requestOrderListByCustomerIdShippingDate(@PathVariable("memberId") String memberId, 
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, 
			@RequestParam(name = "searchDate1", defaultValue = "") String searchDate1,
			@RequestParam(name = "searchDate2", defaultValue = "") String searchDate2,
			Model model) {
		
		String sortField = "orderId";
		String sortWay = "desc";
		Page<Order> paging = this.orederService.getOrderListByCustomerIdAndShippingDate(memberId, searchDate1, searchDate2, pageNum, pageSize, sortField, sortWay);
		List<Order> orderList = paging.getContent();
		
		// 페이지블럭의 시작번호, 끝번호 설정
		int startPage = pageNum - (pageNum -1) % pageBlock;
		int endPage = startPage + pageBlock - 1;
				
		// orderList에 대한 상품명(도서명) -> 주문 내역에 출력
		List<String> bookTitleList = new ArrayList<>();
		for (Order order : orderList) {
			for (OrderItem item : order.getOrderItems().values()) {
				Book book = bookService.getBookByBookId(item.getBookId());
				bookTitleList.add(book.getTitle());
				break;
			}
		}
		orderList.forEach(c -> log.info(c.toString()));
		log.info("주문페이징 : " + orderList);
		log.info("주문사이즈 : " + orderList.size());
		
		// 페이징에서 사용한 값
		model.addAttribute("pageNum", pageNum);                      // 현재 페이지 번호
		model.addAttribute("pageBlock", pageBlock);                  // 페이지블럭수, ex) 5
		model.addAttribute("totalItems", paging.getTotalElements()); // 전체 건수, ex) 165건 
		model.addAttribute("totalPages", paging.getTotalPages());    // 전체 페이지수, ex) 165건 / 10씩 = 16.5 -> 17개 페이지
		model.addAttribute("startPage", startPage);                  // 블럭의 시작페이지
		model.addAttribute("endPage", endPage);                      // 블럭의 끝페이지		

		model.addAttribute("bookTitleList", bookTitleList);
		model.addAttribute("orderList", orderList);

		return "order/orderList";
	}	

	// 주문 상세 조회 (회원 ID와 카트 ID에 따른 주문항목, 주문자, 배송정보 조회)
	@GetMapping("/detail/{orderId}")
	public String requestOrderDetailByCartId(@PathVariable("orderId") Long orderId, Model model) {
		List<OrderItem> orderItemList = this.orderItemService.findByOrderItemId(orderId);
		List<Book> bookList = new ArrayList<>();
		
		for (OrderItem item : orderItemList ) {
			Book book = bookService.getBookByBookId(item.getBookId());
			bookList.add(book);
		}
		
		Order order = this.orederService.getOrderByOrderId(orderId);
		String customerId = order.getCustomer().getCustomerId();
		
		model.addAttribute("customerId", customerId);
		model.addAttribute("bookList", bookList);
		model.addAttribute("orderItemList", orderItemList);
		return "order/orderDetail";
	}
}
