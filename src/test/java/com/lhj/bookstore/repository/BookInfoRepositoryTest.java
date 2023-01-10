package com.lhj.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StopWatch;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import com.lhj.bookstore.config.P6spyLogMessageFormatConfig;
import com.lhj.bookstore.config.QuerydslConfig;
import com.lhj.bookstore.dto.SearchBookInfoDto;
import com.lhj.bookstore.entity.BookInfoEntity;

@DisplayName("도서  Jpa 단위 테스트")
@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration.class)
@Import({P6spyLogMessageFormatConfig.class, QuerydslConfig.class})
class BookInfoRepositoryTest {
	
	@Autowired
	private BookInfoRepository bookInfoRepository;
	
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
			SearchBookInfoDto searchBookInfoDto = new SearchBookInfoDto(keyword, type, 1, 10);
			Pageable pageable = PageRequest.of(searchBookInfoDto.getOffset() -1, searchBookInfoDto.getLimit());
			
			// when
//			List<BookInfoEntity> reuslt = bookInfoRepository.findByTitleContaining(title);
			Page<BookInfoEntity> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoDto, pageable);
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
			SearchBookInfoDto searchBookInfoDto = new SearchBookInfoDto(keyword, type, 1, 10);
			Pageable pageable = PageRequest.of(searchBookInfoDto.getOffset() -1, searchBookInfoDto.getLimit());
			
			// when
			Page<BookInfoEntity> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoDto, pageable);
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
			SearchBookInfoDto searchBookInfoDto = new SearchBookInfoDto();
			Pageable pageable = PageRequest.of(searchBookInfoDto.getOffset() -1, searchBookInfoDto.getLimit());
			
			// when
			Page<BookInfoEntity> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoDto, pageable);
			pagingLog(reuslt);
			
			// then
			assertThat(reuslt.getContent()).isNotEmpty();
			assertThat(reuslt.getSize() > 1);
		}
		
		@Test
		@DisplayName("페이징 최적화 테스트(log확인) - 조회되는 데이터가 limit보다 작으면 count쿼리를 안한다.")
		void searchBookInfo4() {
			
			// given
			SearchBookInfoDto searchBookInfoDto = new SearchBookInfoDto(null, null, 1, 4);
			Pageable pageable = PageRequest.of(searchBookInfoDto.getOffset() -1, searchBookInfoDto.getLimit());
			
			// when
			Page<BookInfoEntity> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoDto, pageable);
			pagingLog(reuslt);
			
			// then
			assertThat(reuslt.getContent()).isNotEmpty();
			assertThat(reuslt.getTotalPages()).isEqualTo(1);
		}
		
		@Test
		@DisplayName("페이징 최적화 테스트(log확인) - 조회되는 데이터가 limit보다 크거나 같으면 count쿼리를 실행한다.")
		void searchBookInfo5() {
			
			// given
			SearchBookInfoDto searchBookInfoDto = new SearchBookInfoDto(null, null, 1, 2);
			Pageable pageable = PageRequest.of(searchBookInfoDto.getOffset() -1, searchBookInfoDto.getLimit());
			
			// when
			Page<BookInfoEntity> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoDto, pageable);
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
		int discount = 10;
		BookInfoEntity bookInfoEntity = bookInfoRepository.findById(1L).orElse(null);
		bookInfoEntity.changeDiscount(discount);
		
		// when
		BookInfoEntity reuslt = bookInfoRepository.findById(1L).orElse(null);
		
		// then
		assertThat(reuslt.getDiscount()).isEqualTo(discount);
	}
	
	@Test
	@DisplayName("도서 단건 조회")
	void getBookInfo() {
		
		// given
		long id = 1L;
		
		// when
		BookInfoEntity reuslt = bookInfoRepository.findById(1L).orElse(null);
		
		// then
		assertThat(reuslt).isNotNull();
	}
	
	/**
	 * 페이징 정보 확인 메소드
	 * @param reuslt
	 */
	void pagingLog(Page<BookInfoEntity> reuslt) {
		System.out.println("total : " + reuslt.getTotalElements());
		System.out.println("pages : " + reuslt.getTotalPages());
		System.out.println("num : " + reuslt.getNumber());
		System.out.println("size : " + reuslt.getSize());
	}
}
