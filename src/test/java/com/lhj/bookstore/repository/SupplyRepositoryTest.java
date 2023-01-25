package com.lhj.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.lhj.bookstore.dto.req.SearchSupBookReq;
import com.lhj.bookstore.dto.res.SupBookRes;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.entity.SupplyBookEntity;
import com.lhj.bookstore.entity.SupplyBookId;
import com.lhj.bookstore.entity.SupplyEntity;

@DisplayName("공급 jpa 단위 테스트")
public class SupplyRepositoryTest extends RepositoryTestCommon {

	@Autowired
	private SupplyRepository supplyRepository;
	
	@Autowired
	private SupplyBookRepository supplyBookRepository;
	
	@Autowired
	private ContractorRepository contractorRepository;
	
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
		// contractor data
		String[] titleArr = {"Hello Java", "JUnit test", "Real MySQL"};
		String[] writerArr = {"홍길동", "이목룡", "임꺽정"};
		// book data
		int[] lowestArr = {10, 20, 30};
		String[] stateCdArr = {"A", "B", "C"};
		
		for (int i = 0; i < titleArr.length; i++) {
			// given
			BookInfoEntity bookInfo = bookInfoRepository.save(BookInfoEntity.builder()
					.title(titleArr[i])
					.type("T00" + (i + 1))
					.supPrice(1000)
					.fixPrice(2000)
					.quantity(100)
					.writer(writerArr[i])
					.discount(5)
					.createdAt(LocalDate.now())
					.build());
			
			ContractorEntity contractor = contractorRepository.save(ContractorEntity.builder()
					.contractAt(LocalDate.now())
					.lowest(lowestArr[i])
					.stateCd(stateCdArr[i])
					.build());
			
			// when
			SupplyEntity supply = supplyRepository.save(SupplyEntity.builder()
					.contractor(contractor)
					.supplyAt(LocalDate.now())
					.build());
			
			supplyBookRepository.save(SupplyBookEntity.builder()
					.supply(supply)
					.bookInfo(bookInfo)
					.build());
		}
	}
	
	@Test
	@DisplayName("공급 등록")
	void registSupply() {
		// given
		ContractorEntity contractor = contractorRepository.findById(1L).orElse(null);
		
		SupplyEntity supply = supplyRepository.save(SupplyEntity.builder()
				.contractor(contractor)
				.supplyAt(LocalDate.now())
				.build());
		
		List<Long> bookIds = Arrays.asList(1L, 2L, 3L);
		for (Long bookId :  bookIds) {
			BookInfoEntity bookInfo = bookInfoRepository.findById(bookId).orElse(null);
			
			supplyBookRepository.save(SupplyBookEntity.builder()
					.supply(supply)
					.bookInfo(bookInfo)
					.build());
		}
		
		// when
		SupplyEntity result = supplyRepository.findById(supply.getId()).orElse(null);
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getSupplyBookList().size()).isEqualTo(bookIds.size());
		assertThat(result.getContractor().getId()).isEqualTo(contractor.getId());
	}
	
	@Nested
	@DisplayName("공급 도서 검색")
	class SearchSupplyBook {
		
		@Test
		@DisplayName("입력한 저자 이름을 포함")
		void searchSupplyBook() {
			// given
			SearchSupBookReq searchSupBookReq = SearchSupBookReq.builder()
					.writer("길동")
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchSupBookReq.getOffset() -1, searchSupBookReq.getLimit());
			
			// when
			Page<SupBookRes> result = supplyRepository.searchSupplyBook(searchSupBookReq, pageable);
			
			// then
			assertThat(result).isNotEmpty();
			assertThat(result.getContent().get(0).getBookInfo().get(0).getWriter()).isEqualTo("홍길동");
		}
		
		@Test
		@DisplayName("입력한 도서 제목을 포함")
		void searchSupplyBook2() {
			// given
			SearchSupBookReq searchSupBookReq = SearchSupBookReq.builder()
					.title("java")
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchSupBookReq.getOffset() -1, searchSupBookReq.getLimit());
			
			// when
			Page<SupBookRes> result = supplyRepository.searchSupplyBook(searchSupBookReq, pageable);
			
			// then
			assertThat(result).isNotEmpty();
			assertThat(result.getContent().get(0).getBookInfo().get(0).getTitle()).isEqualTo("Hello Java");
		}
		
		@Test
		@DisplayName("입력한 도서 타입과 일치")
		void searchSupplyBook3() {
			// given
			String type = "T001";
			SearchSupBookReq searchSupBookReq = SearchSupBookReq.builder()
					.type(type)
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchSupBookReq.getOffset() -1, searchSupBookReq.getLimit());
			
			// when
			Page<SupBookRes> result = supplyRepository.searchSupplyBook(searchSupBookReq, pageable);
			
			// then
			assertThat(result).isNotEmpty();
			assertThat(result.getContent().get(0).getBookInfo().get(0).getType()).isEqualTo(type);
		}
		
		@Test
		@DisplayName("입력한 도서 제목, 저자를 포함 하고  타입이 일치")
		void searchSupplyBook4() {
			// given
			String type = "T001";
			SearchSupBookReq searchSupBookReq = SearchSupBookReq.builder()
					.title("java")
					.writer("길동")
					.type(type)
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchSupBookReq.getOffset() -1, searchSupBookReq.getLimit());
			
			// when
			Page<SupBookRes> result = supplyRepository.searchSupplyBook(searchSupBookReq, pageable);
			
			// then
			assertThat(result).isNotEmpty();
			assertThat(result.getContent().get(0).getBookInfo().get(0).getWriter()).isEqualTo("홍길동");
			assertThat(result.getContent().get(0).getBookInfo().get(0).getTitle()).isEqualTo("Hello Java");
			assertThat(result.getContent().get(0).getBookInfo().get(0).getType()).isEqualTo(type);
		}
	}
	
	@Test
	@DisplayName("공급 내역 삭제")
	void removeSupply() {
		// given
		long supId = 1L;
		Optional<SupplyEntity> supply = supplyRepository.findById(supId);
		long contId = 0L;
		SupplyBookId supBookId = null;
		if (supply.isPresent()) {
			contId = supply.get().getContractor().getId();
			long bookId = supply.get().getSupplyBookList().get(0).getBookInfo().getId();
			supBookId = SupplyBookId.builder()
					.supply(supId)
					.bookInfo(bookId)
					.build();
			
			supply.get().getSupplyBookList().forEach(supplyBookRepository::delete);
			supply.get().deleteSupCont();
			supplyRepository.delete(supply.get());
		}
		
		// when
		ContractorEntity contractor = contractorRepository.findById(contId).orElse(null);
		SupplyBookEntity supplyBook = supplyBookRepository.findById(supBookId).orElse(null);
		SupplyEntity result = supplyRepository.findById(1L).orElse(null);
		
		// then
		assertThat(contractor.getSupplyList()).isEmpty();
		assertThat(supplyBook).isNull();
		assertThat(result).isNull();
	}
	
	@Test
	@DisplayName("공급도서 조회")
	void getSupBook() {
		// given
		long supId = 1L;
		long bookId = 1L;
		SupplyBookId supBookId = SupplyBookId.builder()
					.supply(supId)
					.bookInfo(bookId)
					.build();
		
		// when
		SupplyBookEntity result = supplyBookRepository.findById(supBookId).orElse(null);
		
		// then
		assertThat(result).isNotNull();
		assertThat(result.getSupply().getId()).isEqualTo(supId);
		assertThat(result.getBookInfo().getId()).isEqualTo(bookId);
	}
	
	@Test
	@DisplayName("공급도서 복합키 동등성 테스트")
	void SupplyBookId() {
		// given
		long supId = 1L;
		long bookId = 1L;
		
		// when
		SupplyBookId supBookId1 = SupplyBookId.builder()
				.supply(supId)
				.bookInfo(bookId)
				.build();
	
		SupplyBookId supBookId2 = SupplyBookId.builder()
				.supply(supId)
				.bookInfo(bookId)
				.build();
		
		// then
		assertThat(supBookId1.equals(supBookId2));
		assertThat(supBookId1.hashCode()).isEqualTo(supBookId2.hashCode());
		System.out.println("supBookId1: " + supBookId1.hashCode());
		System.out.println("supBookId2: " + supBookId2.hashCode());
	}
}
