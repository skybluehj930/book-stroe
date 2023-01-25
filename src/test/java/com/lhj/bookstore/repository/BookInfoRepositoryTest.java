package com.lhj.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lhj.bookstore.dto.req.SearchBookInfoReq;
import com.lhj.bookstore.dto.res.BookInfoRes;
import com.lhj.bookstore.entity.BookInfoEntity;

@DisplayName("도서  Jpa 단위 테스트")
class BookInfoRepositoryTest extends RepositoryTestCommon {
	
	@Autowired
	private BookInfoRepository bookInfoRepository;
	
	@BeforeAll
	static void testStart() {
		stopWatch.start();
	}
	
	@AfterAll
	static void testEnd() {
		stopWatch.stop();
		System.out.println("TotalTimeMillis - " + stopWatch.getTotalTimeMillis());
	}
	
	@BeforeEach
	void init() {
		// given
		String[] titleArr = {"Hello Java", "JUnit test", "Real MySQL"};
		String[] writerArr = {"홍길동", "이목룡", "임꺽정"};
		
		for (int i = 0; i < titleArr.length; i++) {
			BookInfoEntity bookInfoEntity = BookInfoEntity.builder()
					.title(titleArr[i])
					.type("T00" + (i + 1))
					.supPrice(1000)
					.fixPrice(2000)
					.quantity(100)
					.writer(writerArr[i])
					.discount(5)
					.createdAt(LocalDate.now())
					.build();
			
			bookInfoRepository.save(bookInfoEntity);
		}
	}
	
	@Test
	@DisplayName("도서 등록")
	void registBookInfo() {
		
		// given
		BookInfoEntity bookInfoEntity = BookInfoEntity.builder()
				.title("Node.js")
				.type("T002")
				.supPrice(1000)
				.fixPrice(2000)
				.quantity(100)
				.writer("이목룡")
				.discount(5)
				.createdAt(LocalDate.now())
				.build();
		
		// when
		BookInfoEntity reuslt = bookInfoRepository.save(bookInfoEntity);
		
		// then
		assertThat(reuslt).isNotNull();
	}
	
	@Nested
	@DisplayName("도서 검색")
	class SearchBookInfo {
		
		@Test
		@DisplayName("type이 일치하고  keyword가 title에  포함되는 도서 조회")
		void searchBookInfo() {
			
			// given
			String keyword = "Mysql"; 
			String type = "T003"; 
			SearchBookInfoReq searchBookInfoReq = SearchBookInfoReq.builder()
					.keyword(keyword)
					.type(type)
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchBookInfoReq.getOffset() -1, searchBookInfoReq.getLimit());
			
			// when
//			List<BookInfoEntity> reuslt = bookInfoRepository.findByTitleContaining(title);
			Page<BookInfoRes> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoReq, pageable);
			pagingLog(reuslt);
			
			// then
			assertThat(reuslt.getContent()).isNotEmpty();
			assertThat(reuslt.getContent().get(0).getTitle()).containsIgnoringCase(keyword);
			assertThat(reuslt.getContent().get(0).getType()).isEqualTo(type);
		}
		
		@Test
		@DisplayName("type이 일치하고  keyword가 writer에  포함되는 도서 조회")
		void searchBookInfo2() {
			
			// given
			String keyword = "길동"; 
			String type = "T001"; 
			SearchBookInfoReq searchBookInfoReq = SearchBookInfoReq.builder()
					.keyword(keyword)
					.type(type)
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchBookInfoReq.getOffset() -1, searchBookInfoReq.getLimit());
			
			// when
			Page<BookInfoRes> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoReq, pageable);
			pagingLog(reuslt);
			
			// then
			assertThat(reuslt.getContent()).isNotEmpty();
			assertThat(reuslt.getContent().get(0).getWriter()).contains(keyword);
			assertThat(reuslt.getContent().get(0).getType()).isEqualTo(type);
		}
		
		@Test
		@DisplayName("조건 값이 모두 null 일 때 - 전체 entity가 조회 된다.")
		void searchBookInfo3() {
			
			// given
			SearchBookInfoReq searchBookInfoReq = SearchBookInfoReq.builder()
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchBookInfoReq.getOffset() -1, searchBookInfoReq.getLimit());
			
			// when
			Page<BookInfoRes> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoReq, pageable);
			pagingLog(reuslt);
			
			// then
			assertThat(reuslt.getContent()).isNotEmpty();
			assertThat(reuslt.getSize() > 1);
		}
		
		@Test
		@DisplayName("페이징 최적화 테스트(log확인) - 조회되는 데이터가 limit보다 작으면 count쿼리를 안한다.")
		void searchBookInfo4() {
			
			// given
			SearchBookInfoReq searchBookInfoReq = SearchBookInfoReq.builder()
					.offset(1)
					.limit(4)
					.build();
			Pageable pageable = PageRequest.of(searchBookInfoReq.getOffset() -1, searchBookInfoReq.getLimit());
			
			// when
			Page<BookInfoRes> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoReq, pageable);
			pagingLog(reuslt);
			
			// then
			assertThat(reuslt.getContent()).isNotEmpty();
			assertThat(reuslt.getTotalPages()).isEqualTo(1);
		}
		
		@Test
		@DisplayName("페이징 최적화 테스트(log확인) - 조회되는 데이터가 limit보다 크거나 같으면 count쿼리를 실행한다.")
		void searchBookInfo5() {
			
			// given
			SearchBookInfoReq searchBookInfoReq = SearchBookInfoReq.builder()
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchBookInfoReq.getOffset() -1, searchBookInfoReq.getLimit());
			
			// when
			Page<BookInfoRes> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoReq, pageable);
			pagingLog(reuslt);
			
			// then
			assertThat(reuslt.getContent()).isNotEmpty();
			assertThat(reuslt.getTotalPages()).isEqualTo(2);
		}
	}
	
	@Test
	@DisplayName("도서 수정")
	void modifyBookInfo() {
		
		// given
		long bookId = 2L;
		int discount = 10;
		BookInfoEntity bookInfoEntity = bookInfoRepository.findById(bookId).orElse(null);
		bookInfoEntity.changeDiscount(discount);
		
		// when
		BookInfoEntity reuslt = bookInfoRepository.findById(bookId).orElse(null);
		
		// then
		assertThat(reuslt).isNotNull();
		assertThat(reuslt.getDiscount()).isEqualTo(discount);
	}
	
	@Test
	@DisplayName("도서 단건 조회")
	void getBookInfo() {
		
		// given
		long bookId = 1L;
		
		// when
		BookInfoRes reuslt = bookInfoRepository.getBookInfo(bookId);
		
		// then
		assertThat(reuslt).isNotNull();
		assertThat(reuslt.getId()).isEqualTo(bookId);
	}
	
	/**
	 * 페이징 정보 확인 메소드
	 * @param reuslt
	 */
	void pagingLog(Page<BookInfoRes> reuslt) {
		System.out.println("total : " + reuslt.getTotalElements());
		System.out.println("pages : " + reuslt.getTotalPages());
		System.out.println("num : " + reuslt.getNumber());
		System.out.println("size : " + reuslt.getSize());
	}
}
