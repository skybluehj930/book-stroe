package com.lhj.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import com.lhj.bookstore.config.P6spyLogMessageFormatConfig;
import com.lhj.bookstore.config.QuerydslConfig;
import com.lhj.bookstore.entity.BookInfoEntity;

@DisplayName("jpa 단위 테스트")
@DataJpaTest(showSql = false)
@ImportAutoConfiguration(DataSourceDecoratorAutoConfiguration.class)
@Import({P6spyLogMessageFormatConfig.class, QuerydslConfig.class})
public class BookInfoRepositoryTest {
	
	@Autowired
	private BookInfoRepository bookInfoRepository;

	@BeforeEach
	void init() {
		// given
		BookInfoEntity bookInfoEntity = BookInfoEntity.builder()
											.title("Hello Java")
											.type("T001")
											.supPrice(1000)
											.fixPrice(2000)
											.quantity(100)
											.writer("홍길동")
											.discount(5)
											.createdAt(LocalDateTime.now())
											.build();
		
		bookInfoRepository.save(bookInfoEntity);
	}
	
	@Test
	@DisplayName("1. 도서 등록")
	void bookRegister() {
		
		// given
		BookInfoEntity bookInfoEntity = BookInfoEntity.builder()
											.title("Hello Java")
											.type("T001")
											.supPrice(1000)
											.fixPrice(2000)
											.quantity(100)
											.writer("홍길동")
											.discount(5)
											.createdAt(LocalDateTime.now())
											.build();
		
		// when
		BookInfoEntity reuslt = bookInfoRepository.save(bookInfoEntity);
		
		// then
		assertThat(reuslt).isNotNull();
	}
	
	@Test
	@DisplayName("2. 도서 조회")
	void bookSearch() {
		
		// given
		String title = "Java";
		
		// when
//		List<BookInfoEntity> reuslt = bookInfoRepository.findByTitleContaining(title);
		List<BookInfoEntity> reuslt = bookInfoRepository.searchBookInfo();
		
		// then
		assertThat(reuslt).isNotEmpty();
	}
	
	@Test
	@DisplayName("3. 도서 수정")
	void bookModify() {
		String title = "Java8";
		
		// given
		BookInfoEntity bookInfoEntity = bookInfoRepository.findById(1L).orElse(null);
		bookInfoEntity.changeTitle(title);
		bookInfoEntity.changeDiscount(10);
		
		// when
		BookInfoEntity reuslt = bookInfoRepository.save(bookInfoEntity);
		
		// then
		assertThat(reuslt.getTitle()).isEqualTo(title);
	}
}
