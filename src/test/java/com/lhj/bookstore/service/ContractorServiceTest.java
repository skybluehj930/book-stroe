package com.lhj.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import com.lhj.bookstore.dto.ContractorDto;
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
			ContractorDto contractorDto = ContractorDto.builder()
					.contractAt("2023-01-14")
					.lowest(lowestArr[i])
					.stateCd(stateCdArr[i])
					.build();
			
			contractorService.registContractor(contractorDto);
		}
	}
	
	@Test
	@DisplayName("계약업체 등록")
	void registContractor() {
		// given
		ContractorDto contractorDto = ContractorDto.builder()
				.contractAt("2023-01-14")
				.lowest(10)
				.stateCd("A")
				.build();
		
		// when
		ContractorEntity reuslt = contractorService.registContractor(contractorDto);
		
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
		ContractorDto contractorDto = ContractorDto.builder()
				.lowest(lowest)
				.stateCd(stateCd)
				.build();
		
		// when
		ContractorEntity reuslt = contractorService.modifyContractor(conId, contractorDto);
		
		// then
		assertThat(reuslt.getId()).isEqualTo(conId);
		assertThat(reuslt.getLowest()).isEqualTo(lowest);
		assertThat(reuslt.getStateCd()).isEqualTo(stateCd);
	}
}
