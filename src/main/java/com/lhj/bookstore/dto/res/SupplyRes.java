package com.lhj.bookstore.dto.res;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SupplyRes {
	
	private Long supId; // 공급번호
	
	private LocalDate supplyAt; // 공급일자

	@Builder
	public SupplyRes(Long supId, LocalDate supplyAt) {
		this.supId = supId;
		this.supplyAt = supplyAt;
	}
}
