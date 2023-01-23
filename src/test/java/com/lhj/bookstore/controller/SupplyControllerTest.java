package com.lhj.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhj.bookstore.dto.req.SupplyReq;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.entity.SupplyBookEntity;
import com.lhj.bookstore.entity.SupplyEntity;
import com.lhj.bookstore.repository.BookInfoRepository;
import com.lhj.bookstore.repository.ContractorRepository;
import com.lhj.bookstore.repository.SupplyBookRepository;
import com.lhj.bookstore.repository.SupplyRepository;

@DisplayName("공급 통합 테스트")
class SupplyControllerTest extends ControllerTestCommon {
	
	@Autowired
	private MockMvc mvc;
	
    @Autowired
    private ObjectMapper objectMapper;
    
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
	void registSupply() throws JsonProcessingException, Exception {
		// given
		SupplyReq supplyReq = SupplyReq.builder()
				.contId(1L)
				.bookIds(Arrays.asList(1L, 2L, 3L))
				.build();
		
		// when
		ResultActions resultActions = mvc.perform(post("/supply")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(supplyReq))
	            .accept(MediaType.APPLICATION_JSON))
	            .andDo(print());
		
		// then
	    resultActions
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.supId").exists())
	            .andExpect(jsonPath("$.supplyAt").exists());
	}

	@Nested
	@DisplayName("공급 도서 검색")
	class SearchSupplyBook {
		
		@Test
		@DisplayName("입력한 저자 이름을 포함")
		void searchSupplyBook() throws Exception {
			// given
			String writer = "길동";
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/supply")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
		            .param("writer", writer)
		            .param("offset", offset)
		            .param("limit", limit)
		            .accept(MediaType.APPLICATION_JSON))
		            .andDo(print());
			
			// then
		    resultActions
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$..bookInfo[?(@.writer == '홍길동')]").exists());
		}
		
		@Test
		@DisplayName("입력한 도서 제목을 포함")
		void searchSupplyBook2() throws Exception {
			// given
		    String title = "java"; 
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/supply")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
		            .param("title", title)
		            .param("offset", offset)
		            .param("limit", limit)
		            .accept(MediaType.APPLICATION_JSON))
		            .andDo(print());
			
			// then
		    resultActions
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$..bookInfo[?(@.title == 'Hello Java')]").exists());
		}
		
		@Test
		@DisplayName("입력한 도서 타입과 일치")
		void searchSupplyBook3() throws Exception {
			// given
			String type = "T001";
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/supply")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
		            .param("type", type)
		            .param("offset", offset)
		            .param("limit", limit)
		            .accept(MediaType.APPLICATION_JSON))
		            .andDo(print());
			
			// then
		    resultActions
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$..bookInfo[?(@.type == '"+ type +"')]").exists());
		}
		
		@Test
		@DisplayName("입력한 도서 제목, 저자를 포함 하고  타입이 일치")
		void searchSupplyBook4() throws Exception {
			// given
			String title = "java";
			String writer = "길동";
			String type = "T001";
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/supply")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
		            .param("title", title)
		            .param("writer", writer)
		            .param("type", type)
		            .param("offset", offset)
		            .param("limit", limit)
		            .accept(MediaType.APPLICATION_JSON))
		            .andDo(print());
			
			// then
		    resultActions
		            .andExpect(status().isOk())
		            .andExpect(jsonPath("$..bookInfo[?(@.writer == '홍길동')]").exists())
		            .andExpect(jsonPath("$..bookInfo[?(@.title == 'Hello Java')]").exists())
		            .andExpect(jsonPath("$..bookInfo[?(@.type == '"+ type +"')]").exists());
		}
	}
	
	@Test
	@DisplayName("공급 내역 삭제")
	void removeSupply() throws Exception {
		// given
		int supId = 1;
		
		// when
		ResultActions resultActions = mvc.perform(delete("/supply/" + supId)
	            .accept(MediaType.APPLICATION_JSON))
	            .andDo(print());
		
		// then
	    resultActions
	            .andExpect(status().isOk());
	    
	    SupplyEntity result = supplyRepository.findById(new Long(supId)).orElse(null);
	    assertThat(result).isNull();
	}
}
