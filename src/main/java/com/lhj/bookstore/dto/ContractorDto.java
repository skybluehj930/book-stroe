package com.lhj.bookstore.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContractorDto {
	
	private String contractAt; // 계약일자
	
	private Integer lowest; // 최저가 비율
	
	private String stateCd; // 상태 코드
	
	@Builder
	public ContractorDto(String contractAt, Integer lowest, String stateCd) {
		this.contractAt = contractAt;
		this.lowest = lowest;
		this.stateCd = stateCd;
	}
}
