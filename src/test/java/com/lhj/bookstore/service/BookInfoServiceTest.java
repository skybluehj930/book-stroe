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
@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration.class)
@Import({P6spyLogMessageFormatConfig.class, QuerydslConfig.class, BookInfoService.class})
class BookInfoServiceTest {
	
	@Autowired
	private BookInfoService bookInfoService;
	
	private static StopWatch stopWatch = new StopWatch();
	
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
			SearchBookInfoDto searchBookInfoDto = new SearchBookInfoDto(keyword, type, 1, 10);
			
			// when
			Page<BookInfoEntity> reuslt = bookInfoService.searchBookInfo(searchBookInfoDto);
			
			// then
			assertThat(reuslt.getContent()).isNotNull();
			assertThat(reuslt.getContent().get(0).getTitle()).containsIgnoringCase(keyword);
			assertThat(reuslt.getContent().get(0).getType()).isEqualTo(type);
		}
		
		@Test
		@DisplayName("type이 일치하고  keyword가 writer에  포함되는 도서 조회")
		void searchBookInfo2() {
			
			// given
			String keyword = "길동"; 
			String type = "T001"; 
			SearchBookInfoDto searchBookInfoDto = new SearchBookInfoDto(keyword, type, 1, 10);
			
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
		BookInfoDto bookInfoDto = BookInfoDto.builder()
				.id(1L)
				.quantity(1)
				.discount(10)
				.supPrice(3000)
				.build();
		
		bookInfoService.modifyBookInfo(bookInfoDto);
		
		// when
		BookInfoEntity reuslt = bookInfoService.getBookInfo(1L);
		
		// then
		assertThat(reuslt).isNotNull();
		assertThat(reuslt.getQuantity()).isEqualTo(1);
		assertThat(reuslt.getDiscount()).isEqualTo(10);
		assertThat(reuslt.getSupPrice()).isEqualTo(3000);
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