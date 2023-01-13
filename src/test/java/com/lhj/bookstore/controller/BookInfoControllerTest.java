package com.lhj.bookstore.controller;

import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhj.bookstore.dto.BookInfoDto;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.repository.BookInfoRepository;

@DisplayName("도서 통합 테스트")
class BookInfoControllerTest extends ControllerTestCommon {
	
	@Autowired
	private MockMvc mvc;
	
    @Autowired
    private ObjectMapper objectMapper;
    
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
	void registBookInfo() throws Exception {
		
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
		ResultActions resultActions = mvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookInfoDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
		
		// then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(bookInfoDto.getTitle()));
	}
	
	
	@Nested
	@DisplayName("도서 검색")
	class SearchBookInfo {
		
		@Test
		@DisplayName("type이 일치하고  keyword가 title에  포함되는 도서 조회")
		void searchBookInfo() throws Exception {
			
			// given
			String keyword = "java"; 
			String type = "T001"; 
			
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("keyword", keyword);
			params.add("type", type);
			params.add("offset", "1");
			params.add("limit", "10");
			
			// when
			ResultActions resultActions = mvc.perform(get("/book")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
					.params(params)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print());
			
			// then
			resultActions
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.content.length()", equalTo(1)))
				.andExpect(jsonPath("$.content[0].title", containsStringIgnoringCase(keyword)))
				.andExpect(jsonPath("$.content[0].type", equalTo(type)));
		}
		
		@Test
		@DisplayName("type이 일치하고  keyword가 writer에  포함되는 도서 조회")
		void searchBookInfo2() throws Exception {
			
			// given
			String keyword = "길동"; 
			String type = "T001"; 
			
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("keyword", keyword);
			params.add("type", type);
			params.add("offset", "1");
			params.add("limit", "10");
			
			// when
			ResultActions resultActions = mvc.perform(get("/book")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
					.params(params)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print());
			
			// then
			resultActions
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.content.length()", equalTo(1)))
				.andExpect(jsonPath("$.content[0].title", containsStringIgnoringCase(keyword)))
				.andExpect(jsonPath("$.content[0].type", equalTo(type)));
		}
		
		@Test
		@DisplayName("조건 값이 모두 null 일 때 - 전체 entity가 조회 된다.")
		void searchBookInfo3() throws Exception {
			
			// given
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			
			// when
			ResultActions resultActions = mvc.perform(get("/book")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
					.params(params)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(print());
			
			// then
			resultActions
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.content.length()", equalTo(3)));
		}

	}
	
	@Test
	@DisplayName("도서 수정")
	void modifyBookInfo() throws Exception {
		
		// given
		int bookId = 1;
		BookInfoDto bookInfoDto = BookInfoDto.builder()
				.quantity(1)
				.discount(10)
				.supPrice(3000)
				.build();
		
		// when
		ResultActions resultActions = mvc.perform(patch("/book/{bookId}", bookId)
				.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookInfoDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
		
		// then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(bookId)))
                .andExpect(jsonPath("$.quantity", equalTo(bookInfoDto.getQuantity())))
                .andExpect(jsonPath("$.discount", equalTo(bookInfoDto.getDiscount())))
                .andExpect(jsonPath("$.supPrice", equalTo(bookInfoDto.getSupPrice())));
	}
	
	@Test
	@DisplayName("도서 단건 조회")
	void getBookInfo() throws Exception {
		
		// given
		int bookId = 1;
		
		// when
		ResultActions resultActions = mvc.perform(get("/book/{bookId}", bookId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
				
		// then
        resultActions
                .andExpect(status().isOk())
        		.andExpect(jsonPath("$.id", equalTo(bookId)));
	}

}
