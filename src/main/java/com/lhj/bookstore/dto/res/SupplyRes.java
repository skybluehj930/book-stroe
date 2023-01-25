package com.lhj.bookstore.dto.res;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SupplyRes {
	
	@Schema(description = "공급번호", required = true, example = "1")
	private Long supId; // 공급번호
	
	@Schema(description = "공급일자", required = true, example = "2023-01-25")
	private LocalDate supplyAt; // 공급일자

	@Builder
	public SupplyRes(Long supId, LocalDate supplyAt) {
		this.supId = supId;
		this.supplyAt = supplyAt;
	}
}
