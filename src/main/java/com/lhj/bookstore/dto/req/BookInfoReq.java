package com.lhj.bookstore.dto.req;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfoReq {
	
	private String title; // 도서명
	
	private String type; // 도서구분
	
	private Integer quantity; // 수량
	
	private Integer supPrice; // 공급단가
	
	private String writer; // 저자
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt; // 발행일자
	
	private Integer fixPrice; // 정가
	
	private Integer discount; // 적용할인율
	
	@Builder
	public BookInfoReq(String title
			, String type 
			, Integer quantity
			, Integer supPrice 
			, String writer
			, Integer fixPrice
			, Integer discount
			, LocalDate createdAt) {
		
		this.title = title;
		this.type = type;
		this.quantity = quantity;
		this.supPrice = supPrice;
		this.writer = writer;
		this.fixPrice = fixPrice;
		this.discount = discount;
		this.createdAt = createdAt;
	}
}
