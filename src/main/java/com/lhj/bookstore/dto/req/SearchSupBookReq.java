package com.lhj.bookstore.dto.req;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // @ModelAttribute로 값을 받기 위해 setter 적용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchSupBookReq {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startAt; // 시작 일자
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endAt; // 종료 일자

	private String title; // 도서명
	
	private String type; // 도서구분
	
	private String writer; // 저자
	
	private Integer offset = 1;
	
	private Integer limit = 10;
	
	@Builder
	public SearchSupBookReq(String title
				, LocalDate startAt
				, LocalDate endAt
				, String type
				, String writer
				, Integer offset
				, Integer limit) {
		
		this.title = title;
		this.startAt = startAt;
		this.endAt = endAt;
		this.type = type;
		this.writer = writer;
		this.offset = offset;
		this.limit = limit;
	}
}
