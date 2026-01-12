package com.sbproject.mall01.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sbproject.mall01.entity.Member;
import com.sbproject.mall01.entity.Review;
import com.sbproject.mall01.service.MemberService;
import com.sbproject.mall01.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
@RequestMapping("/review")
public class ReviewController {
	
	private final ReviewService reviewService;
	private final MemberService memberService;
//	private final BookService bookService;
	
//	public ReviewController(MemberService memberService, ReviewService reviewService, BookService bookService) {
//		this.memberService = memberService;
//		this.reviewService = reviewService;
//		this.bookService = bookService;
//	}
	
	// 리뷰 등록
	@PostMapping("/add")
	public String requestAddReview(@Valid @ModelAttribute("review") Review review, 
			BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "book/book";
		}
		// 리뷰 저장
		Member member = memberService.findByMemberId(review.getWriter().getMemberId());		
		review.setWriter(member);
		review.setCreateDate(LocalDate.now());
		reviewService.saveReview(review);		

		return "redirect:/books/book/" + review.getBookId();
	}
	
	// 리뷰 조회
	
	
}
