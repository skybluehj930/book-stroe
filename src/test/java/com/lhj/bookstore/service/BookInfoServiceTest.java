package com.lhj.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.util.StopWatch;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import com.lhj.bookstore.config.P6spyLogMessageFormatConfig;
import com.lhj.bookstore.config.QuerydslConfig;
import com.lhj.bookstore.dto.BookInfoDto;
import com.lhj.bookstore.dto.SearchBookInfoDto;
import com.lhj.bookstore.entity.BookInfoEntity;

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
			BookInfoDto bookInfoDto = BookInfoDto.builder()
					.title(titleArr[i])
					.type("T00" + (i + 1))
					.supPrice(1000)
					.fixPrice(2000)
					.quantity(100)
					.writer(writerArr[i])
					.discount(5)
					.createdAt("2023-01-10")
					.build();
			
			bookInfoService.registBookInfo(bookInfoDto);
		}
	}
	
	@Test
	@DisplayName("도서 등록")
	public void registBookInfo() {
		
		// given
		BookInfoDto bookInfoDto = BookInfoDto.builder()
				.title("Node.js")
				.type("T002")
				.supPrice(1000)
				.fixPrice(2000)
				.quantity(100)
				.writer("이목룡")
				.discount(5)
				.createdAt("2023-01-10")
				.build();
		
		// when
		BookInfoEntity result = bookInfoService.registBookInfo(bookInfoDto);
		
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
			SearchBookInfoDto searchBookInfoDto = SearchBookInfoDto.builder()
					.keyword(keyword)
					.type(type)
					.offset(1)
					.limit(10)
					.build();
			
			// when
			Page<BookInfoEntity> reuslt = bookInfoService.searchBookInfo(searchBookInfoDto);
			
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
			SearchBookInfoDto searchBookInfoDto = SearchBookInfoDto.builder()
					.keyword(keyword)
					.type(type)
					.offset(1)
					.limit(10)
					.build();
			
			// when
			Page<BookInfoEntity> reuslt = bookInfoService.searchBookInfo(searchBookInfoDto);
			
			// then
			assertThat(reuslt.getContent()).isNotEmpty();
			assertThat(reuslt.getContent().get(0).getWriter()).contains(keyword);
			assertThat(reuslt.getContent().get(0).getType()).isEqualTo(type);
		}
		
		@Test
		@DisplayName("조건 값이 모두 null 일 때 - 전체 entity가 조회 된다.")
		void searchBookInfo3() {
			
			// given
			SearchBookInfoDto searchBookInfoDto = new SearchBookInfoDto();
			
			// when
			Page<BookInfoEntity> reuslt = bookInfoService.searchBookInfo(searchBookInfoDto);
			
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
		BookInfoDto bookInfoDto = BookInfoDto.builder()
				.quantity(1)
				.discount(10)
				.supPrice(3000)
				.build();
		
		bookInfoService.modifyBookInfo(bookId, bookInfoDto);
		
		// when
		BookInfoEntity reuslt = bookInfoService.getBookInfo(bookId);
		
		// then
		assertThat(reuslt).isNotNull();
		assertThat(reuslt.getId()).isEqualTo(bookId);
		assertThat(reuslt.getQuantity()).isEqualTo(bookInfoDto.getQuantity());
		assertThat(reuslt.getDiscount()).isEqualTo(bookInfoDto.getDiscount());
		assertThat(reuslt.getSupPrice()).isEqualTo(bookInfoDto.getSupPrice());
	}
	
	@Test
	@DisplayName("도서 단건 조회")
	void getBookInfo() {
		
		// given
		long id = 1L;
		
		// when
		BookInfoEntity reuslt = bookInfoService.getBookInfo(id);
		
		// then
		assertThat(reuslt).isNotNull();
	}
}
