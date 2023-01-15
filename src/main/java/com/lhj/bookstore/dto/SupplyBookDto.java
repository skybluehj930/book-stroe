package com.lhj.bookstore.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SupplyBookDto {
	
	private Long supId; // 공급번호
	
	private LocalDate supplyAt; // 공급일자
	
	private Long bookId; // 도서번호
	
	private String title; // 도서명
	
	private String type; // 도서구분
	
	private Integer supPrice; // 공급단가
	
	private String writer; // 저자
	
	private LocalDate createdAt; // 발행일자
	
	private Integer fixPrice; // 정가
	
	private Integer discount; // 적용할인율
}
