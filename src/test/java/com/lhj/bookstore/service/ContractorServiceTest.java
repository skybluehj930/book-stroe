package com.lhj.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import com.lhj.bookstore.dto.req.ContractorReq;
import com.lhj.bookstore.dto.res.ContractorRes;
import com.lhj.bookstore.entity.ContractorEntity;

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
		ContractorRes reuslt = contractorService.registContractor(contractorReq);
		
		// then
		assertThat(reuslt).isNotNull();
	}

	@Test
	@DisplayName("계약업체 조회")
	void findContractor() {
		// given
		int offset = 1;
		int limit = 10;
		
		// when
		Page<ContractorEntity> reuslt = contractorService.findContractor(offset, limit);
		
		// then
		assertThat(reuslt.getTotalElements()).isEqualTo(3);
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
		ContractorRes reuslt = contractorService.modifyContractor(conId, contractorReq);
		
		// then
		assertThat(reuslt.getId()).isEqualTo(conId);
		assertThat(reuslt.getLowest()).isEqualTo(lowest);
		assertThat(reuslt.getStateCd()).isEqualTo(stateCd);
	}
}
