package com.lhj.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.repository.BookInfoRepository;

@ExtendWith(MockitoExtension.class)
class BookStoreServiceTest {
	
	@Mock
	private BookInfoRepository bookInfoRepository;
	
	@InjectMocks
	private BookStoreService bookStoreService;
	
	@BeforeEach
	@DisplayName("1. 도서 등록")
	public void bookRegister() {
		
		// given
		BookInfoEntity bookInfoEntity = new BookInfoEntity();
		bookInfoEntity.setTitle("Hello Java");
		bookInfoEntity.setType("T001");	
		bookInfoEntity.setSupPrice(1000);
		bookInfoEntity.setFixPrice(2000);
		bookInfoEntity.setQuantity(100);
		bookInfoEntity.setWriter("홍길동");
		bookInfoEntity.setDiscount(5);
		bookInfoEntity.setCreatedAt(new Date());
		when(bookInfoRepository.save(bookInfoEntity)).thenReturn(bookInfoEntity);
		
		// when
		BookInfoEntity result = bookStoreService.bookRegister(bookInfoEntity);
		
		// then
		assertThat(result).isNotNull();
	}
	
	
	@Test
	@DisplayName("2. 도서 조회")
	public void bookSearch() {
		BookInfoEntity bookInfoEntity = new BookInfoEntity();
		bookInfoEntity.setTitle("Hello Java");
		bookInfoEntity.setType("T001");	
		bookInfoEntity.setSupPrice(1000);
		bookInfoEntity.setFixPrice(2000);
		bookInfoEntity.setQuantity(100);
		bookInfoEntity.setWriter("홍길동");
		bookInfoEntity.setDiscount(5);
		bookInfoEntity.setCreatedAt(new Date());
		List<BookInfoEntity> list = new ArrayList<BookInfoEntity>();
		list.add(bookInfoEntity);
		
		// given
		String title = "Java";
		when(bookInfoRepository.findByTitleContaining(title)).thenReturn(list);
		
		// when
		List<BookInfoEntity> bookInfoList = bookStoreService.findByTitleContaining(title);
		
		// then
		assertThat(bookInfoList.get(0).getTitle())
			.isNotNull()
			.contains(title);
	}
}
