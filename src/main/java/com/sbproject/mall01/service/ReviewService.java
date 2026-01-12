package com.sbproject.mall01.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sbproject.mall01.entity.Review;
import com.sbproject.mall01.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ReviewRepository reviewRepository;
	
//	public ReviewService(ReviewRepository reviewRepository) {
//		this.reviewRepository = reviewRepository;
//	}
	
	// 리뷰 등록
	public void saveReview(Review review) {
		this.reviewRepository.save(review);
	}
	
	// 리뷰 목록 조회 (bookId별로 조회)
	public List<Review> getReviewListByBookId(String bookId) {
		return this.reviewRepository.findByBookIdOrderByIdDesc(bookId);
	}
	
}
