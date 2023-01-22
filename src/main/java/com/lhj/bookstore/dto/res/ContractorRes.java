package com.lhj.bookstore.dto.res;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContractorRes {

	private Long id; // 계약번호
	
	private LocalDate contractAt; // 계약일자
	
	private Integer lowest; // 최저가 비율
	
	private String stateCd; // 상태 코드
	
	@Builder
	public ContractorRes(Long id, LocalDate contractAt, Integer lowest, String stateCd) {
		this.id = id;
		this.contractAt = contractAt;
		this.lowest = lowest;
		this.stateCd = stateCd;
	}
}
