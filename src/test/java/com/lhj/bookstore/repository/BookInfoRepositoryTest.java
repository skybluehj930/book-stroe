package com.lhj.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

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

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import com.lhj.bookstore.config.P6spyLogMessageFormatConfig;
import com.lhj.bookstore.config.QuerydslConfig;
import com.lhj.bookstore.dto.SearchBookInfoDto;
import com.lhj.bookstore.entity.BookInfoEntity;

import lombok.extern.slf4j.Slf4j;

@DisplayName("도서  Jpa 단위 테스트")
@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration.class)
@Import({P6spyLogMessageFormatConfig.class, QuerydslConfig.class})
class BookInfoRepositoryTest {
	
	@Autowired
	private BookInfoRepository bookInfoRepository;
	
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
					.createdAt(LocalDateTime.now())
					.build();
			
			bookInfoRepository.save(bookInfoEntity);
		}
	}
	
	
	@Test
	@DisplayName("1. 도서 등록")
	void bookRegister() {
		
		// given
		BookInfoEntity bookInfoEntity = BookInfoEntity.builder()
				.title("Node.js")
				.type("T002")
				.supPrice(1000)
				.fixPrice(2000)
				.quantity(100)
				.writer("이목룡")
				.discount(5)
				.createdAt(LocalDateTime.now())
				.build();
		
		// when
		BookInfoEntity reuslt = bookInfoRepository.save(bookInfoEntity);
		
		// then
		assertThat(reuslt).isNotNull();
	}
	
	@Nested
	@DisplayName("2. 도서 조회")
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
			Pageable pageable = PageRequest.of(searchBookInfoDto.getOffset() -1, searchBookInfoDto.getLimit());
			
			// when
			Page<BookInfoEntity> reuslt = bookInfoRepository.searchBookInfo(searchBookInfoDto, pageable);
			pagingLog(reuslt);
			
			// then
			assertThat(reuslt.getContent()).isNotNull();
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
			assertThat(reuslt.getContent()).isNotNull();
			assertThat(reuslt.getSize() > 1);
		}
	}
	
	@Test
	@DisplayName("3. 도서 수정")
	void bookModify() {
		int discount = 10;
		
		// given
		BookInfoEntity bookInfoEntity = bookInfoRepository.findById(1L).orElse(null);
		bookInfoEntity.changeDiscount(10);
		
		// when
		BookInfoEntity reuslt = bookInfoRepository.findById(1L).orElse(null);
		
		// then
		assertThat(reuslt.getDiscount()).isEqualTo(discount);
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
