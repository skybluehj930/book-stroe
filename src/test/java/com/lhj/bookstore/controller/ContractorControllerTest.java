package com.lhj.bookstore.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhj.bookstore.dto.ContractorDto;
import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.repository.ContractorRepository;

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
		ContractorDto contractorDto = ContractorDto.builder()
				.contractAt("2023-01-14")
				.lowest(10)
				.stateCd("A")
				.build();
		
		// when
		ResultActions resultActions = mvc.perform(post("/contractor")
				.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contractorDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
		
		// then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.stateCd").value(contractorDto.getStateCd()));
	}
	
	@Test
	@DisplayName("계약업체 조회")
	void findContractor() throws Exception {
		// given
		String offset = "1";
		String limit = "10";
		
		// when
		ResultActions resultActions = mvc.perform(get("/contractor")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
				.param("offset", offset)
				.param("limit", limit)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print());
		
		// then
		resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()", equalTo(3)));
	}
	
	@Test
	@DisplayName("계약업체 수정")
	void modifyContractor() throws JsonProcessingException, Exception {
		// given
		int conId = 1;
		int lowest = 20;
		String stateCd = "B";
		ContractorDto contractorDto = ContractorDto.builder()
				.lowest(lowest)
				.stateCd(stateCd)
				.build();
		
		// when
		ResultActions resultActions = mvc.perform(patch("/contractor/{conId}", conId)
				.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contractorDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
		
		// then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(conId)))
                .andExpect(jsonPath("$.lowest", equalTo(contractorDto.getLowest())))
                .andExpect(jsonPath("$.stateCd", equalTo(contractorDto.getStateCd())));
	}
}
