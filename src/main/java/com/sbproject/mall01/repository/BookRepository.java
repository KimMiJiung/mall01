package com.sbproject.mall01.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sbproject.mall01.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {	
	
	// 도서ID를 통한 상세보기 (1건)
	Optional<Book> findByBookId(String bookId);
	
	// 전체 목록 조회 -> 페이징 처리
	Page<Book> findAll(Pageable pageable);

	// 필드별로 도서 목록을 조회하는 메서드 (최근 출판일 순으로 정렬) -> 페이징 처리를 함
	// - 저자별  도서 목록
	Page<Book> findByAuthorOrderByReleaseDateDesc(String keyword, Pageable pageable);

	// - 출판사별 도서 목록
	Page<Book> findByPublisherOrderByReleaseDateDesc(String keyword, Pageable pageable);
	
	// - 도서 분류별 도서 목록
	Page<Book> findByCategoryOrderByReleaseDateDesc(String keyword, Pageable pageable);
	
	// - 도서 상태별 도서 목록
	Page<Book> findByBookConditionOrderByReleaseDateDesc(String keyword, Pageable pageable);
	
	// - 검색을 통한 도서 목록 조회 : 제목, 내용, 저자, 출판사의 4가지 필드를 조회, 제목에 대한 오름차순
	Page<Book> findByTitleContainingOrDescriptionContainingOrAuthorContainingOrPublisherOrderByTitleAsc(String keyword1, String keyword2, String keyword3,String keyword4, Pageable pageable);

	// 메인 상단 슬라이더 1 - 최신 상품 (category별로 최신상품 20)
	// - books.html 사용
	public final String SELECT_CATEGORY_COUNT = 
		"select * from (select *, row_number() over (partition by category order by release_date desc) as rn from book ) as ranking "
		+ "where ranking.rn <= :count";
	@Query(value = SELECT_CATEGORY_COUNT, nativeQuery = true)
	List<Book> querySelectCategoryCount(@Param("count") int count);
	
	
	// 하단 슬라이더 2 - 추천상품: 건강, 요리, 사랑 키워드 별로 제목 또는 내용에서 조회해서 5건씩 
	// - 제목, 내용으로 조회
	// - books.html 사용
	public List<Book> findTop5ByTitleContainingOrDescriptionContainingOrderByReleaseDateDesc(String keyword1, String keyword2);
	
	// 상세 하단 슬라이드 1 - 제목, 저자, 상품분류 별로 5개씩 관련 상품
	// - 제목은 파싱해서 조회, 저자, 상품분류는 그대로 조회
	// - 중복 데이터 제거 필요
	public List<Book> findTop5ByAuthorOrderByReleaseDateDesc(String author);
	public List<Book> findTop5ByCategoryOrderByReleaseDateDesc(String category);
	public List<Book> findTop5ByTitleContainingOrderByReleaseDateDesc(String category);
	
	public final String UPDATE_STOCK = 
			"update book set stock = stock - :quantity  where book_id = :bookId";
	@Transactional @Modifying	
	@Query(value = UPDATE_STOCK, nativeQuery = true)
	void queryUpdateStock(@Param("quantity") int quantity, @Param("bookId") String bookId);
	
	public final String SELECT_STOCK = 
			"select stock from book where book_id = :bookId";
		@Query(value = SELECT_STOCK, nativeQuery = true)
	int querySelectStock(@Param("bookId") String bookId);
}
