package com.sbproject.mall01.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sbproject.mall01.entity.Book;
import com.sbproject.mall01.entity.Review;
import com.sbproject.mall01.service.BookService;
import com.sbproject.mall01.service.ReviewService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/books")
public class BookController {

	private final BookService bookService;
	private final ReviewService reviewService;
	
	// 페이징 설정
	private int pageSize = 12;  // 페이지당 상품 개수 (4개씩 3줄)	
	private int pageBlock = 5;  // 페이지 블럭수 (보여지는 페이지 블럭이 5개씩)
	
	// [1] 일반 사용자 기능
	
	// 도서 전체 목록 조회 -> 페이징 처리가 되지 않음
	/*
	@GetMapping
	public String requestBookList(Model model) {
		List<Book> bookList = this.bookService.getBookList();
		model.addAttribute("bookList", bookList);
		return "book/books";
	}
	*/
	
	// 도서 전체 목록 조회 -> 페이징 처리된 메서드로 이동
	@GetMapping
	public String requestBookList(Model model) {
		return requestBookListPaging(1, "all", null,  model);

	}
	
	// 저자, 출판사, 도서분류, 도서상태 별로 도서 조회 -> 페이징 처리된 메서드로 이동
	// - 출판일을 기준으로 내림차순 (최근 출판일 순으로 정렬)
	@GetMapping("/state")
	public String requestBookListByState(
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "state", defaultValue = "all") String state,
			@RequestParam(name = "keyword") String keyword,
			Model model) {

		return requestBookListPaging(pageNum, state, keyword, model);
	}
	
	// 검색을 통한 도서 조회 -> 페이징 처리된 메서드로 이동
	// 제목, 내용, 저자, 출판사의 4가지 필드에 대한 검색 -> 제목에 대한 오름차순
	@PostMapping("/search")
	public String requestBookListBySearch(
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(name = "state", defaultValue = "search") String state,
			@RequestParam(name = "keyword") String keyword,	
			Model model) {
		
		return requestBookListPaging(pageNum, state, keyword, model);
	}
	
	
	// 도서 전체 목록 조회 -> 페이징 처리를 함
	// defaultValue: 해당 매개변수의 값이 없을 때 사용하는 값
	@GetMapping("/paging")
	public String requestBookListPaging(
				@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
				@RequestParam(name = "state", defaultValue = "all") String state,
				@RequestParam(name = "keyword") String keyword,
				Model model) {
		// 출판일을 기준으로 최근 순으로 보여줌 
		Page<Book> paging = bookService.getBookList(pageNum, state, keyword, pageSize, "releaseDate", "desc");

		// 페이징 처리된 도서 목록 획득
		List<Book> bookList = paging.getContent();
		
		// 페이지블럭의 시작번호, 끝번호 설정
		int startPage = pageNum - (pageNum -1) % pageBlock;
		int endPage = startPage + pageBlock - 1;
		
		// 페이징 정보를 모델에 저장
		model.addAttribute("bookList", bookList);  					 // 도서 목록
		model.addAttribute("pageNum", pageNum);                      // 현재 페이지 번호
		model.addAttribute("pageBlock", pageBlock);                  // 페이지블럭수, ex) 5
		model.addAttribute("totalItems", paging.getTotalElements()); // 전체 건수, ex) 165건 
		model.addAttribute("totalPages", paging.getTotalPages());    // 전체 페이지수, ex) 165건 / 12씩 = 13.75 -> 14개 페이지
		model.addAttribute("startPage", startPage);                  // 블럭의 시작페이지
		model.addAttribute("endPage", endPage);                      // 블럭의 끝페이지
		model.addAttribute("state", state);                          // all, author, publisher, category, bookCondition 중의 1개의 값
		model.addAttribute("keyword", keyword);                      // state에서 사용할 값
		
		// 상단 슬라이더 - 최신 상품 (category별로 최신상품 20)
		List<Book> sliderList1 = this.bookService.getBookListSlider("main1");
		model.addAttribute("sliderList1", sliderList1);
		//sliderList1.forEach(c -> log.info(c.toString()));
				
		// 하단 슬라이더 2 - 추천상품: 건강, 요리, 사랑 키워드 별로 제목 또는 내용에서 조회해서 5건씩 
		// - 제목, 내용으로 조회
		// - books.html 사용
		List<Book> sliderList2 = this.bookService.getBookListSlider("main2");
		model.addAttribute("sliderList2", sliderList2);
		//sliderList2.forEach(c -> log.info(c.toString()));
		
		List<Book> sliderList4 = this.bookService.getBookListSlider("mainAll");
		model.addAttribute("sliderList4", sliderList4);
		
		return "book/books";
	}
	
	// 도서 상세 보기 (1권)
	// 1번 방법 - PathVariable 사용
	@GetMapping("/book/{bookId}")
	public String requestBookById(@PathVariable("bookId") String bookId, Model model) {
		Book book = this.bookService.getBookByBookId(bookId);
		model.addAttribute("book", book);
		log.info(bookId);
		// 리뷰 목록 정보 획득
		List<Review> reviewList = reviewService.getReviewListByBookId(book.getBookId());
		
		// 아이디 보호(첫글자와 마지막 글자 이외에는 *로 변경)
		for (int i = 0; i < reviewList.size(); i++) {
			String rId = reviewList.get(i).getWriter().getMemberId();
			String sId = "";
			sId += rId.charAt(0);
			for (int j = 1; j < rId.length() - 1; j++) {
				sId += "*";
			}
			sId += rId.charAt(rId.length() - 1);
			log.info(sId);
			reviewList.get(i).getWriter().setMemberId(sId);
		}

		model.addAttribute("reviewList", reviewList);
				
		// 하단 슬라이더 - 관련 상품 (제목, 저자, 상품분류 별로 5개씩 관련 상품)
		List<Book> sliderList3 = this.bookService.getBookListSlider2("detail1", book);
		model.addAttribute("sliderList3", sliderList3);
		return "/book/book";
	}
	
	// 2번 방법 - RequestParam 사용
	/*
	@GetMapping("/book")
	public String requestBookById(@RequestParam("bookId") String bookId, Model model) {
		Book book = this.bookService.getBookByBookId(bookId);
		model.addAttribute("book", book);
		return "/book/book";
	}
	*/
	
	// 파일이미지 다운로드
	@GetMapping("/download")
	public void downloadBookImage(@RequestParam("file") String file, 
			HttpServletResponse response) throws IOException {
		File imageFile = new File(fileDir + file);
		response.setContentType("application/download");
		response.setContentLength((int)imageFile.length());
		response.setHeader("Content-disposition", "attachment;filename=\"" + file + "\"");
		
		OutputStream os = response.getOutputStream();
		FileInputStream fis = new FileInputStream(imageFile);
		FileCopyUtils.copy(fis, os);
		
		fis.close();
		os.close();
	}
	

	
	// 도서ID에 해당하는 도서 1건 조회
	
	// #########################################################################
	// [2] 관리자 기능
	
	// 도서 등록
	// 도서 등록 폼
	@GetMapping("/admin/add")
	public String requestAddBookForm(Model model) {
		model.addAttribute("book", new Book());
		return "/book/addBook";
	}
	
	private String fileDir = "e:/upload/";
	
	// 도서 등록 처리
	@PostMapping("/admin/add")
	public String requestAddBookProc(@Valid @ModelAttribute Book book, BindingResult bindingResult) {
		// 유효성 검사를 위배했을 때
		if (bindingResult.hasErrors()) {
			return "book/addBook";
		}
		
		// 업로드 파일 처리
		MultipartFile bookImage = book.getBookImage();
		String saveName = bookImage.getOriginalFilename();
		File saveFile = new File(fileDir, saveName);
		
		if(bookImage != null && !bookImage.isEmpty()) {
			try {
				bookImage.transferTo(saveFile);
			} catch(IOException e) {
				e.printStackTrace();
				throw new RuntimeException("도서 이미지 업로드를 실패하였습니다.!", e);
			}
			book.setFileName(saveName);
		} else {
			book.setFileName("no_image.jpg");
		}
		this.bookService.addNewBook(book);
		return "redirect:/books";
	}
	
	// 폼에서 넘어오는 name의 값이 ModelAttribute에 설정되어 있는 객체에 정확하게 바인딩되도록 하는 메서드
	// @InitBinder에서 생략하는 값은 @ModelAttribute에서 값을 설정하지 않음	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("bookId", "title", "price", "author", "description", "publisher", 
				"category", "stock", "releaseDate", "bookCondition","fileName", "bookImage");
	}
			
	// 도서 전체 목록 조회
	
	// 도서 1건 조회
	
	// 도서 삭제
	
	// 도서 수정

}
