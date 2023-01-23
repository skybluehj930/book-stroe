package com.lhj.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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
import com.jayway.jsonpath.JsonPath;
import com.lhj.bookstore.dto.req.ContractorReq;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.entity.SupplyBookEntity;
import com.lhj.bookstore.entity.SupplyEntity;
import com.lhj.bookstore.repository.BookInfoRepository;
import com.lhj.bookstore.repository.ContractorRepository;
import com.lhj.bookstore.repository.SupplyBookRepository;
import com.lhj.bookstore.repository.SupplyRepository;

@DisplayName("계약업체 통합 테스트")
class ContractorControllerTest extends ControllerTestCommon {

	@Autowired
    private MockMvc mvc;
	
    @Autowired
    private ObjectMapper objectMapper;
    
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
	void registContractor() throws Exception {
		// given
		ContractorReq contractorReq = ContractorReq.builder()
				.contractAt(LocalDate.now())
				.lowest(10)
				.stateCd("A")
				.build();
		
		// when
		ResultActions resultActions = mvc.perform(post("/contractor")
				.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contractorReq))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
		
		// then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.stateCd").value(contractorReq.getStateCd()));
	}
	
	@Test
	@DisplayName("계약업체 검색")
	void findContractor() throws Exception {
		// given
		String stateCd = "A";
		LocalDate startAt = LocalDate.now();
		LocalDate endAt = LocalDate.now();
		String offset = "1";
		String limit = "10";
		
		// when
		ResultActions resultActions = mvc.perform(get("/contractor")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("stateCd", stateCd)
				.param("startAt", startAt.toString())
				.param("endAt", endAt.toString())
				.param("offset", offset)
				.param("limit", limit)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
		
		// then
		resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].stateCd", equalTo(stateCd)))
            .andExpect(jsonPath("$.content[?(@.contractAt >= '"+ startAt.toString() +"')]").exists())
			.andExpect(jsonPath("$.content[?(@.contractAt <= '"+ endAt.toString() +"')]").exists());
		
		String json = resultActions.andReturn().getResponse().getContentAsString();
		LocalDate contractAt = LocalDate.parse(JsonPath.read(json, "$.content[0].contractAt"));
		assertThat(contractAt.isAfter(startAt) || contractAt.isEqual(startAt)); // 주어진 날짜가, 파라미터로 전달받은 날짜보다 크거나 같을 경우.
		assertThat(contractAt.isBefore(endAt) || contractAt.isEqual(endAt)); // 주어진 날짜가, 파라미터로 전달받은 날짜보다 작거나 같을 경우.
	}
	
	@Test
	@DisplayName("계약업체 정보 변경")
	void changeContractor() throws JsonProcessingException, Exception {
		// given
		int contId = 1;
		int lowest = 20;
		String stateCd = "B";
		ContractorReq contractorReq = ContractorReq.builder()
				.lowest(lowest)
				.stateCd(stateCd)
				.build();
		
		// when
		ResultActions resultActions = mvc.perform(put("/contractor/"+ contId)
				.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contractorReq))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
		
		// then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(contId)))
                .andExpect(jsonPath("$.lowest", equalTo(contractorReq.getLowest())))
                .andExpect(jsonPath("$.stateCd", equalTo(contractorReq.getStateCd())));
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
		void searchContSupBook() throws Exception {
			// given 
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/contractor/supply-book")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
					.param("offset", offset)
					.param("limit", limit)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print());
			
			// then
			resultActions
            	.andExpect(status().isOk())
            	.andExpect(jsonPath("$.content.length()", equalTo(3)))
				.andExpect(jsonPath("$..[?(@.bookInfo.length() == 0)]").doesNotExist());
		}
		
		@Test
		@DisplayName("입력한 도서 제목을 포함 ")
		void searchContSupBook2() throws Exception {
			// given 
			String title = "java";
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/contractor/supply-book")
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
		@DisplayName("입력한 저자 이름을 포함")
		void searchContSupBook3() throws Exception {
			// given
			String writer = "길동";
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/contractor/supply-book")
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
		@DisplayName("입력한 도서 타입과 일치")
		void searchContSupBook4() throws Exception {
			// given
			String type = "T001";
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/contractor/supply-book")
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
		@DisplayName("입력한 계약업체 상태코드와 일치")
		void searchContSupBook5() throws Exception {
			// given
			String stateCd = "A";
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/contractor/supply-book")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
					.param("stateCd", stateCd)
					.param("offset", offset)
					.param("limit", limit)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print());
			
			// then
			resultActions
	        	.andExpect(status().isOk())
	        	.andExpect(jsonPath("$..[?(@.stateCd == '"+ stateCd +"')]").exists());
		}
		
		@Test
		@DisplayName("입력한 도서 제목을 포함하고, 계약업체 상태코드와 일치")
		void searchContSupBook6() throws Exception {
			// given
			String stateCd = "B";
			String title = "TEst";
			String offset = "1";
			String limit = "10";
			
			// when
			ResultActions resultActions = mvc.perform(get("/contractor/supply-book")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
					.param("stateCd", stateCd)
					.param("title", title)
					.param("offset", offset)
					.param("limit", limit)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print());
			
			// then
			resultActions
	        	.andExpect(status().isOk())
	        	.andExpect(jsonPath("$..[?(@.stateCd == '"+ stateCd +"')]").exists())
	        	.andExpect(jsonPath("$..bookInfo[?(@.title == 'JUnit test')]").exists());
		}
		
		@Test
		@DisplayName("계약업체 공급된 도서와 상관 없이 단건 조회 - 도서가 있는 경우")
		void getContSupBook() throws Exception {
			// given 
			int contId = 4;
			
			// when
			ResultActions resultActions = mvc.perform(get("/contractor/supply-book/{contId}", contId)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print());
			
			// then
			resultActions
        		.andExpect(status().isOk())
				.andExpect(jsonPath("$.bookInfo[0].id", notNullValue()));
		}
	}
	
	@Test
	@DisplayName("계약업체 공급된 도서와 상관 없이 단건 조회 - 도서가 없는 경우")
	void getContSupBook() throws Exception {
		// given 
		int contId = 1;
		
		// when
		ResultActions resultActions = mvc.perform(get("/contractor/supply-book/{contId}", contId)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
		
		// then
		resultActions
    		.andExpect(status().isOk())
			.andExpect(jsonPath("$.bookInfo[0].id", nullValue()));
	}
}
