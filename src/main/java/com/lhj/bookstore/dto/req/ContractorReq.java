package com.lhj.bookstore.dto.req;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractorReq {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate contractAt; // 계약일자
	
	private Integer lowest; // 최저가 비율
	
	private String stateCd; // 상태 코드
	
	@Builder
	public ContractorReq(LocalDate contractAt, Integer lowest, String stateCd) {
		this.contractAt = contractAt;
		this.lowest = lowest;
		this.stateCd = stateCd;
	}
}
