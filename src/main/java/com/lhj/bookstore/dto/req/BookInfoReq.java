package com.lhj.bookstore.dto.req;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfoReq {
	
	@Schema(description = "도서명", required = true, example = "Hellow Java")
	private String title;
	
	@Schema(description = "도서구분", required = true, example = "T001", allowableValues = {"T001", "T002", "T003"})
	private String type;
	
	@Schema(description = "수량", required = true, example = "100")
	private Integer quantity;
	
	@Schema(description = "공급단가", required = true, example = "2000")
	private Integer supPrice;
	
	@Schema(description = "저자", required = true, example = "홍길동")
	private String writer;
	
	@Schema(description = "발행일자", required = true, example = "2023-01-25")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createdAt;
	
	@Schema(description = "정가", required = true, example = "1000")
	private Integer fixPrice;
	
	@Schema(description = "적용할인율(%)", required = true, example = "10")
	private Integer discount;
	
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
