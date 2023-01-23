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

import com.lhj.bookstore.dto.req.ContractorReq;
import com.lhj.bookstore.dto.req.SearchContReq;
import com.lhj.bookstore.dto.req.SearchContSupBookReq;
import com.lhj.bookstore.dto.res.ContSupBookRes;
import com.lhj.bookstore.dto.res.ContractorRes;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.entity.SupplyBookEntity;
import com.lhj.bookstore.entity.SupplyEntity;
import com.lhj.bookstore.repository.BookInfoRepository;
import com.lhj.bookstore.repository.ContractorRepository;
import com.lhj.bookstore.repository.SupplyBookRepository;
import com.lhj.bookstore.repository.SupplyRepository;

@DisplayName("계약업체 Service to Jpa 테스트")
@Import(ContractorService.class)
class ContractorServiceTest extends ServiceTestCommon {
	
	@Autowired
	private ContractorService contractorService;
	
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
		int[] lowestArr = {10, 20, 30};
		String[] stateCdArr = {"A", "B", "C"};
		
		for (int i = 0; i < stateCdArr.length; i++) {
			ContractorReq contractorReq = ContractorReq.builder()
					.contractAt(LocalDate.now())
					.lowest(lowestArr[i])
					.stateCd(stateCdArr[i])
					.build();
			
			contractorService.registContractor(contractorReq);
		}
	}
	
	@Test
	@DisplayName("계약업체 등록")
	void registContractor() {
		// given
		ContractorReq contractorReq = ContractorReq.builder()
				.contractAt(LocalDate.now())
				.lowest(10)
				.stateCd("A")
				.build();
		
		// when
		ContractorRes result = contractorService.registContractor(contractorReq);
		
		// then
		assertThat(result).isNotNull();
	}

	@Test
	@DisplayName("계약업체 검색")
	void searchContractor() {
		// given
		String stateCd = "A";
		// 특정 날짜 지장
//		LocalDate startAt = LocalDate.parse("2023-01-22");
//		LocalDate endAt = LocalDate.parse("2023-01-22");
		// 현재 날짜
		LocalDate startAt = LocalDate.now();
		LocalDate endAt = LocalDate.now();
		SearchContReq searchContReq = SearchContReq.builder()
				.stateCd(stateCd)
				.startAt(startAt)
				.endAt(endAt)
				.offset(1)
				.limit(10)
				.build();
		
		// when
		Page<ContractorRes> result = contractorService.searchContractor(searchContReq);
		
		// then
		assertThat(result).isNotEmpty();
		assertThat(result.getContent().get(0).getStateCd()).isEqualTo(stateCd);
		assertThat(result.getContent().get(0).getContractAt().isAfter(startAt) 
					|| result.getContent().get(0).getContractAt().isEqual(startAt));
		assertThat(result.getContent().get(0).getContractAt().isBefore(endAt) 
					|| result.getContent().get(0).getContractAt().isEqual(endAt));
	}
	
	@Test
	@DisplayName("계약업체 수정")
	void modifyContractor() {
		// given
		long conId = 1L;
		int lowest = 20;
		String stateCd = "B";
		ContractorReq contractorReq = ContractorReq.builder()
				.lowest(lowest)
				.stateCd(stateCd)
				.build();
		
		// when
		ContractorRes result = contractorService.modifyContractor(conId, contractorReq);
		
		// then
		assertThat(result.getId()).isEqualTo(conId);
		assertThat(result.getLowest()).isEqualTo(lowest);
		assertThat(result.getStateCd()).isEqualTo(stateCd);
	}
	
	@Nested
	@DisplayName("계약업체별 공급된 도서 검색")
	class SearchContSupBook {
		
		@Autowired 
		private ContractorRepository contractorRepository;
		
		@Autowired
		private SupplyRepository supplyRepository;
		
		@Autowired
		private SupplyBookRepository supplyBookRepository;
		
		@Autowired
		private BookInfoRepository bookInfoRepository;
		
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
		@DisplayName("검색 조건이 없을 때")
		void searchContSupBook() {
			// given 
			SearchContSupBookReq searchContSupBookReq = SearchContSupBookReq.builder()
					.offset(1)
					.limit(10)
					.build();
			
			// when
			Page<ContSupBookRes> result = contractorService.searchContSupBook(searchContSupBookReq);
			
			// then
			assertThat(result.getTotalElements()).isEqualTo(3);
			result.forEach(b -> assertThat(b.getBookInfo()).isNotEmpty());
		}
		
		@Test
		@DisplayName("입력한 도서 제목을 포함 ")
		void searchContSupBook2() {
			// given
			SearchContSupBookReq searchContSupBookReq = SearchContSupBookReq.builder()
					.title("java")
					.offset(1)
					.limit(10)
					.build();
			
			// when
			Page<ContSupBookRes> result = contractorService.searchContSupBook(searchContSupBookReq);
			
			// then
			assertThat(result.getContent().get(0).getBookInfo()
					.get(0).getTitle()).isEqualTo("Hello Java");
		}
		
		@Test
		@DisplayName("입력한 저자 이름을 포함")
		void searchContSupBook3() {
			// given
			SearchContSupBookReq searchContSupBookReq = SearchContSupBookReq.builder()
					.writer("길동")
					.offset(1)
					.limit(10)
					.build();
			
			// when
			Page<ContSupBookRes> result = contractorService.searchContSupBook(searchContSupBookReq);
			
			// then
			assertThat(result.getContent().get(0).getBookInfo()
					.get(0).getWriter()).isEqualTo("홍길동");
		}
		
		@Test
		@DisplayName("입력한 도서 타입과 일치")
		void searchContSupBook4() {
			// given
			String type = "T001";
			SearchContSupBookReq searchContSupBookReq = SearchContSupBookReq.builder()
					.type(type)
					.offset(1)
					.limit(10)
					.build();
			
			// when
			Page<ContSupBookRes> result = contractorService.searchContSupBook(searchContSupBookReq);
			
			// then
			assertThat(result.getContent().get(0).getBookInfo()
					.get(0).getType()).isEqualTo(type);
		}
		
		@Test
		@DisplayName("입력한 계약업체 상태코드와 일치")
		void searchContSupBook5() {
			// given
			String stateCd = "A";
			SearchContSupBookReq searchContSupBookReq = SearchContSupBookReq.builder()
					.stateCd(stateCd)
					.offset(1)
					.limit(10)
					.build();
			
			// when
			Page<ContSupBookRes> result = contractorService.searchContSupBook(searchContSupBookReq);
			
			// then
			assertThat(result.getContent().get(0).getStateCd()).isEqualTo(stateCd);
		}
		
		@Test
		@DisplayName("입력한 도서 제목을 포함하고, 계약업체 상태코드와 일치")
		void searchContSupBook6() {
			// given
			String stateCd = "B";
			SearchContSupBookReq searchContSupBookReq = SearchContSupBookReq.builder()
					.title("TEst")
					.stateCd(stateCd)
					.offset(1)
					.limit(10)
					.build();
			
			// when
			Page<ContSupBookRes> result = contractorService.searchContSupBook(searchContSupBookReq);
			
			// then
			assertThat(result.getContent().get(0).getStateCd()).isEqualTo(stateCd);
			assertThat(result.getContent().get(0).getBookInfo()
					.get(0).getTitle()).isEqualTo("JUnit test");
		}
		
		@Test
		@DisplayName("계약업체 공급된 도서와 상관 없이 단건 조회 - 도서가 있는 경우")
		void getContSupBook() {
			// given 
			long contId = 4L;
			
			// when
			ContSupBookRes result = contractorService.getContSupBook(contId);
			
			// then
			assertThat(result.getContId()).isEqualTo(contId);
			assertThat(result.getBookInfo().get(0).getTitle()).isEqualTo("Hello Java");
		}
	}
	
	@Test
	@DisplayName("계약업체 공급된 도서와 상관 없이 단건 조회 - 도서가 없는 경우")
	void getContSupBook() {
		// given 
		long contId = 1L;
		
		// when
		ContSupBookRes result = contractorService.getContSupBook(contId);
		
		// then
		assertThat(result.getContId()).isEqualTo(contId);
		assertThat(result.getBookInfo().get(0).getTitle()).isNull();
	}
}
