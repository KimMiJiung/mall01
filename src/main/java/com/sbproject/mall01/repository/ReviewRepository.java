package com.sbproject.mall01.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbproject.mall01.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	// 리뷰 목록 조회 (bookId별로 조회)
	List<Review> findByBookIdOrderByIdDesc(String bookId);
}
