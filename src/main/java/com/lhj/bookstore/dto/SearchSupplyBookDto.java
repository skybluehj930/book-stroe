package com.lhj.bookstore.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // @ModelAttribute로 값을 받기 위해 setter 적용
@NoArgsConstructor
public class SearchSupplyBookDto {
	
	private Long supBookId; // 공급도서번호
	
	private Long supId; // 공급번호
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate supplyAt; // 공급일자

	private String title; // 도서명
	
	private String type; // 도서구분
	
	private String writer; // 저자
	
	private Integer offset = 1;
	
	private Integer limit = 10;
	
	@Builder
	public SearchSupplyBookDto(Long supId
				, String title
				, LocalDate supplyAt
				, String type
				, String writer
				, Integer offset
				, Integer limit) {
		
		this.supId = supId;
		this.title = title;
		this.supplyAt = supplyAt;
		this.type = type;
		this.writer = writer;
		this.offset = offset;
		this.limit = limit;
	}
}
