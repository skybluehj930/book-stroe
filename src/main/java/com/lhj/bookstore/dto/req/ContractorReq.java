package com.lhj.bookstore.dto.req;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractorReq {
	
	@Schema(description = "계약일자", required = true, example = "2023-01-25")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate contractAt;
	
	@Schema(description = "최저가 비율", required = true, example = "10")
	private Integer lowest;
	
	@Schema(description = "상태 코드", required = true, example = "A", allowableValues = {"A", "B", "C"})
	private String stateCd;
	
	@Builder
	public ContractorReq(LocalDate contractAt, Integer lowest, String stateCd) {
		this.contractAt = contractAt;
		this.lowest = lowest;
		this.stateCd = stateCd;
	}
}
