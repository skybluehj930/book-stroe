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

import com.lhj.bookstore.dto.req.SearchContSupBookReq;
import com.lhj.bookstore.dto.req.SearchContReq;
import com.lhj.bookstore.dto.res.ContSupBookRes;
import com.lhj.bookstore.dto.res.ContractorRes;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.entity.SupplyBookEntity;
import com.lhj.bookstore.entity.SupplyEntity;

@DisplayName("계약업체 jpa 단위 테스트")
public class ContractorRepositoryTest extends RepositoryTestCommon {
	
	@Autowired
	private ContractorRepository contractorRepository;
	
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
			ContractorEntity contractorEntity = ContractorEntity.builder()
					.contractAt(LocalDate.now())
					.lowest(lowestArr[i])
					.stateCd(stateCdArr[i])
					.build();
			
			contractorRepository.save(contractorEntity);
		}
	}

	@Test
	@DisplayName("계약업체 등록")
	void registContractor() {
		// given
		ContractorEntity contractorEntity = ContractorEntity.builder()
				.contractAt(LocalDate.now())
				.lowest(10)
				.stateCd("A")
				.build();
		
		// when
		ContractorEntity reuslt = contractorRepository.save(contractorEntity);
		
		// then
		assertThat(reuslt).isNotNull();
	}
	
	@Test
	@DisplayName("계약업체 검색")
	void earchContractor() {
		// given
		String stateCd = "A";
		LocalDate startAt = LocalDate.parse("2023-01-22");
		LocalDate endAt = LocalDate.parse("2023-01-22");
		SearchContReq searchContReq = SearchContReq.builder()
				.stateCd(stateCd)
				.startAt(startAt)
				.endAt(endAt)
				.offset(1)
				.limit(10)
				.build();
		Pageable pageable = PageRequest.of(searchContReq.getOffset() -1, searchContReq.getLimit());
		
		// when
		Page<ContractorRes> reuslt = contractorRepository.searchContractor(searchContReq, pageable);
		
		// then
		assertThat(reuslt).isNotEmpty();
		assertThat(reuslt.getContent().get(0).getStateCd()).isEqualTo(stateCd);
		assertThat(reuslt.getContent().get(0).getContractAt().isAfter(startAt) 
					|| reuslt.getContent().get(0).getContractAt().isEqual(startAt));
		assertThat(reuslt.getContent().get(0).getContractAt().isBefore(endAt) 
					|| reuslt.getContent().get(0).getContractAt().isEqual(endAt));
	}
	
	@Test
	@DisplayName("계약업체 수정")
	void modifyContractor() {
		// given
		long conId = 1L;
		int lowest = 20;
		String stateCd = "B";
		
		ContractorEntity contractorEntity = contractorRepository.findById(conId).orElse(null);
		contractorEntity.changeLowest(lowest);
		contractorEntity.changeStateCd(stateCd);
		
		// when
		ContractorEntity reuslt = contractorRepository.findById(conId).orElse(null);
		
		// then
		assertThat(reuslt.getId()).isEqualTo(conId);
		assertThat(reuslt.getLowest()).isEqualTo(lowest);
		assertThat(reuslt.getStateCd()).isEqualTo(stateCd);
	}
	
	@Nested
	@DisplayName("계약업체별 공급된 도서 검색")
	class SearchContSupBook {
		
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
		@DisplayName("jpa로 조회")
		void contracterSupplyBook() {
			// given
			int offset = 1;
			int limit = 10;
			Pageable pageable = PageRequest.of(offset -1, limit);
			
			// when
			Page<ContractorEntity> reuslt = contractorRepository.findAll(pageable);
			
			// then
			assertThat(reuslt.getContent().get(3).getId()).isEqualTo(4L);
			assertThat(reuslt.getContent().get(3).getSupplyList()).isNotEmpty();
			assertThat(reuslt.getContent().get(3)
					.getSupplyList().get(0).getSupplyBookList()).isNotEmpty();
			assertThat(reuslt.getContent().get(3)
					.getSupplyList().get(0)
					.getSupplyBookList().get(0)
					.getBookInfo().getTitle()).isEqualTo("Hello Java");
		}
		
		@Test
		@DisplayName("검색 조건이 없을 때")
		void searchContSupBook() {
			// given 
			SearchContSupBookReq searchContSupBookDto = SearchContSupBookReq.builder()
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchContSupBookDto.getOffset() -1, searchContSupBookDto.getLimit());
			
			// when
			Page<ContSupBookRes> reuslt = contractorRepository.searchContSupBook(searchContSupBookDto, pageable);
			
			// then
			assertThat(reuslt.getTotalElements()).isEqualTo(3);
			reuslt.forEach(b -> assertThat(b.getBookInfo()).isNotEmpty());
		}
		
		@Test
		@DisplayName("입력한 도서 제목을 포함 ")
		void searchContSupBook2() {
			// given
			SearchContSupBookReq searchContSupBookDto = SearchContSupBookReq.builder()
					.title("java")
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchContSupBookDto.getOffset() -1, searchContSupBookDto.getLimit());
			
			// when
			Page<ContSupBookRes> reuslt = contractorRepository.searchContSupBook(searchContSupBookDto, pageable);
			
			// then
			assertThat(reuslt.getContent().get(0).getBookInfo()
					.get(0).getTitle()).isEqualTo("Hello Java");
		}
		
		@Test
		@DisplayName("입력한 저자 이름을 포함")
		void searchContSupBook3() {
			// given
			SearchContSupBookReq searchContSupBookDto = SearchContSupBookReq.builder()
					.writer("길동")
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchContSupBookDto.getOffset() -1, searchContSupBookDto.getLimit());
			
			// when
			Page<ContSupBookRes> reuslt = contractorRepository.searchContSupBook(searchContSupBookDto, pageable);
			
			// then
			assertThat(reuslt.getContent().get(0).getBookInfo()
					.get(0).getWriter()).isEqualTo("홍길동");
		}
		
		@Test
		@DisplayName("입력한 도서 타입과 일치")
		void searchContSupBook4() {
			// given
			String type = "T001";
			SearchContSupBookReq searchContSupBookDto = SearchContSupBookReq.builder()
					.type(type)
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchContSupBookDto.getOffset() -1, searchContSupBookDto.getLimit());
			
			// when
			Page<ContSupBookRes> reuslt = contractorRepository.searchContSupBook(searchContSupBookDto, pageable);
			
			// then
			assertThat(reuslt.getContent().get(0).getBookInfo()
					.get(0).getType()).isEqualTo(type);
		}
		
		@Test
		@DisplayName("입력한 계약업체 상태코드와 일치")
		void searchContSupBook5() {
			// given
			String stateCd = "A";
			SearchContSupBookReq searchContSupBookDto = SearchContSupBookReq.builder()
					.stateCd(stateCd)
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchContSupBookDto.getOffset() -1, searchContSupBookDto.getLimit());
			
			// when
			Page<ContSupBookRes> reuslt = contractorRepository.searchContSupBook(searchContSupBookDto, pageable);
			
			// then
			assertThat(reuslt.getContent().get(0).getStateCd()).isEqualTo(stateCd);
		}
		
		@Test
		@DisplayName("입력한 도서 제목을 포함하고, 계약업체 상태코드와 일치")
		void searchContSupBook6() {
			// given
			String stateCd = "B";
			SearchContSupBookReq searchContSupBookDto = SearchContSupBookReq.builder()
					.title("TEst")
					.stateCd(stateCd)
					.offset(1)
					.limit(10)
					.build();
			Pageable pageable = PageRequest.of(searchContSupBookDto.getOffset() -1, searchContSupBookDto.getLimit());
			
			// when
			Page<ContSupBookRes> reuslt = contractorRepository.searchContSupBook(searchContSupBookDto, pageable);
			
			// then
			assertThat(reuslt.getContent().get(0).getStateCd()).isEqualTo(stateCd);
			assertThat(reuslt.getContent().get(0).getBookInfo()
					.get(0).getTitle()).isEqualTo("JUnit test");
		}
		
		@Test
		@DisplayName("계약업체 공급된 도서와 상관 없이 단건 조회 - 도서가 있는 경우")
		void getContSupBook() {
			// given 
			long contId = 4L;
			
			// when
			ContSupBookRes result = contractorRepository.getContSupBook(contId);
			
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
		ContSupBookRes result = contractorRepository.getContSupBook(contId);
		
		assertThat(result.getContId()).isEqualTo(contId);
		assertThat(result.getBookInfo().get(0).getId()).isNull();
	}
}
