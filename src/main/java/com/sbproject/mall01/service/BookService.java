package com.sbproject.mall01.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sbproject.mall01.entity.Book;
import com.sbproject.mall01.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookService {

	private final BookRepository bookRepository;

	// [1] 일반 사용자 기능

	// 전체 도서 목록 조회 -> 페이징 처리를 하지 않음
	public List<Book> getBookList() {
		return this.bookRepository.findAll();
	}

	// 1.전체 도서 목록 조회 -> 페이징 처리를 함
	// - pageNum: 현재 페이지 번호
	// - sortField: 정렬할 필드명
	// - sortWay: 정렬 방법 (오름차순 또는 내림차순)
	// 2.저자, 출판사, 도서분류, 도서상태 별로 도서 조회 -> 페이징 처리를 함
	// - 출판일을 기준으로 내림차순 (최근 출판일 순으로 정렬)	
	// 3. 검색을 통한 도서 조회 -> 페이징 처리를 함
	// - 제목, 내용, 저자, 출판사의 4가지 조건을 가지고 조회
	public Page<Book> getBookList(int pageNum, String state, String keyword, int pageSize, String sortField, String sortWay) {
		Pageable pageable = PageRequest.of(pageNum-1, pageSize, 
				sortWay.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());

		switch(state) {
		case "author": 
			return bookRepository.findByAuthorOrderByReleaseDateDesc(keyword, pageable);
		case "publisher": 
			return bookRepository.findByPublisherOrderByReleaseDateDesc(keyword, pageable);
		case "category": 
			return bookRepository.findByCategoryOrderByReleaseDateDesc(keyword, pageable);
		case "bookCondition": 
			return bookRepository.findByBookConditionOrderByReleaseDateDesc(keyword, pageable);
		case "search":  // search일 때 처리(검색)			
			return bookRepository.findByTitleContainingOrDescriptionContainingOrAuthorContainingOrPublisherOrderByTitleAsc(keyword, keyword, keyword, keyword, pageable);
		default:        // all일 때 처리
			return bookRepository.findAll(pageable);  
		}
	}

	// 도서ID에 해당하는 도서 1건 조회
	public Book getBookByBookId(String bookId) {
		return this.bookRepository.findByBookId(bookId).get();
	}
	
	// 메인보기에서 슬라이더에서 사용할 메서드
	public List<Book> getBookListSlider(String slider) {
		List<Book> sliderList2 = new ArrayList<Book>();
		switch(slider) {
		case "main1":
			return this.bookRepository.querySelectCategoryCount(3);
		case "main2":
			List<Book> b1 = this.bookRepository.findTop5ByTitleContainingOrDescriptionContainingOrderByReleaseDateDesc("건강", "건강");
			List<Book> b2 = this.bookRepository.findTop5ByTitleContainingOrDescriptionContainingOrderByReleaseDateDesc("요리", "요리");
			List<Book> b3 = this.bookRepository.findTop5ByTitleContainingOrDescriptionContainingOrderByReleaseDateDesc("사랑", "사랑");
			sliderList2.addAll(b1);
			sliderList2.addAll(b2);
			sliderList2.addAll(b3);
			return sliderList2;
		default:
			return this.bookRepository.findAll();
		}
	}
	
	// 상세보기 슬라이더에서 사용할 메서드
	public List<Book> getBookListSlider2(String slider, Book book) {
		switch(slider) {
		case "detail1" :
			List<Book> b1 = this.bookRepository.findTop5ByAuthorOrderByReleaseDateDesc(book.getAuthor());
			List<Book> b2 = this.bookRepository.findTop5ByCategoryOrderByReleaseDateDesc(book.getCategory());
			
			List<Book> b3 = new ArrayList<>();
			String word = book.getTitle();
			String[] words = word.split(" ");
			for (String w : words) {
				b3.addAll(this.bookRepository.findTop5ByTitleContainingOrderByReleaseDateDesc(w));
			}
			List<Book> sliderList3 = new ArrayList<>();
			sliderList3.addAll(b1);
			sliderList3.addAll(b2);
			sliderList3.addAll(b3);
			return sliderList3;
		default:
			return null;
		}
	}
	
	@Transactional
	@Modifying
	public void updateStock(int orderQuantity, String bookId) {
		this.bookRepository.queryUpdateStock(orderQuantity, bookId);
	}	
	
	public int getStock(String orderBookId) {
		int stock = bookRepository.querySelectStock(orderBookId);
		return stock;
	}

	// ##############################################################################
	// [2] 관리자 기능

	// 도서 등록
	public void addNewBook(Book book) {
		bookRepository.save(book);
	}
	
	

	// 전체 도서 목록 조회

	// 도서 1권 조회

	// 도서 정보 수정

	// 도서 삭제
}
