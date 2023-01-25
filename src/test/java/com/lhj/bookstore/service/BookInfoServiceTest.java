package com.lhj.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import com.lhj.bookstore.dto.req.BookInfoReq;
import com.lhj.bookstore.dto.req.SearchBookInfoReq;
import com.lhj.bookstore.dto.res.BookInfoRes;

@DisplayName("도서 Service to Jpa 테스트")
@Import(BookInfoService.class)
class BookInfoServiceTest extends ServiceTestCommon {
	
	@Autowired
	private BookInfoService bookInfoService;
	
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
			BookInfoReq bookInfoReq = BookInfoReq.builder()
					.title(titleArr[i])
					.type("T00" + (i + 1))
					.supPrice(1000)
					.fixPrice(2000)
					.quantity(100)
					.writer(writerArr[i])
					.discount(5)
					.createdAt(LocalDate.parse("2023-01-10"))
					.build();
			
			bookInfoService.registBookInfo(bookInfoReq);
		}
	}
	
	@Test
	@DisplayName("도서 등록")
	public void registBookInfo() {
		
		// given
		BookInfoReq bookInfoReq = BookInfoReq.builder()
				.title("Node.js")
				.type("T002")
				.supPrice(1000)
				.fixPrice(2000)
				.quantity(100)
				.writer("이목룡")
				.discount(5)
				.createdAt(LocalDate.parse("2023-01-10"))
				.build();
		
		// when
		BookInfoRes result = bookInfoService.registBookInfo(bookInfoReq);
		
		// then
		assertThat(result).isNotNull();
	}
	
	@Nested
	@DisplayName("도서 검색")
	class SearchBookInfo {
		
		@Test
		@DisplayName("type이 일치하고  keyword가 title에  포함되는 도서 조회")
		void searchBookInfo() {
			
			// given
			String keyword = "java"; 
			String type = "T001"; 
			SearchBookInfoReq searchBookInfoReq = SearchBookInfoReq.builder()
					.keyword(keyword)
					.type(type)
					.offset(1)
					.limit(10)
					.build();
			
			// when
			Page<BookInfoRes> reuslt = bookInfoService.searchBookInfo(searchBookInfoReq);
			
			// then
			assertThat(reuslt.getContent()).isNotNull();
			assertThat(reuslt.getTotalElements()).isEqualTo(1);
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
			
			// when
			Page<BookInfoRes> reuslt = bookInfoService.searchBookInfo(searchBookInfoReq);
			
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
			
			// when
			Page<BookInfoRes> reuslt = bookInfoService.searchBookInfo(searchBookInfoReq);
			
			// then
			assertThat(reuslt.getContent()).isNotEmpty();
			assertThat(reuslt.getSize() > 1);
		}
	}
	
	@Test
	@DisplayName("도서 수정")
	void modifyBookInfo() {
		
		// given
		long bookId = 1L;
		BookInfoReq bookInfoReq = BookInfoReq.builder()
				.quantity(1)
				.discount(10)
				.supPrice(3000)
				.build();
		
		bookInfoService.modifyBookInfo(bookId, bookInfoReq);
		
		// when
		BookInfoRes reuslt = bookInfoService.getBookInfo(bookId);
		
		// then
		assertThat(reuslt).isNotNull();
		assertThat(reuslt.getId()).isEqualTo(bookId);
		assertThat(reuslt.getQuantity()).isEqualTo(bookInfoReq.getQuantity());
		assertThat(reuslt.getDiscount()).isEqualTo(bookInfoReq.getDiscount());
		assertThat(reuslt.getSupPrice()).isEqualTo(bookInfoReq.getSupPrice());
	}
	
	@Test
	@DisplayName("도서 단건 조회")
	void getBookInfo() {
		
		// given
		long bookId = 1L;
		
		// when
		BookInfoRes reuslt = bookInfoService.getBookInfo(bookId);
		
		// then
		assertThat(reuslt).isNotNull();
		assertThat(reuslt.getId()).isEqualTo(bookId);
	}
}
