package com.lhj.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lhj.bookstore.entity.ContractorEntity;

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
	@DisplayName("계약업체 조회")
	void findContractor() {
		// given
		int offset = 1;
		int limit = 10;
		Pageable pageable = PageRequest.of(offset -1, limit);
		
		// when
		Page<ContractorEntity> reuslt = contractorRepository.findAll(pageable);
		
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
}
