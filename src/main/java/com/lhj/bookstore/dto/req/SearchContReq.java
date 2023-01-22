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
public class SearchContReq {
	
	private String stateCd; // 상태 코드
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startAt; // 시작 일자
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endAt; // 종료 일자
	
	private Integer offset = 1;
	
	private Integer limit = 10;
	
	@Builder
	public SearchContReq(String stateCd, LocalDate startAt, LocalDate endAt, Integer offset, Integer limit) {
		this.stateCd = stateCd;
		this.startAt = startAt;
		this.endAt = endAt;
		this.offset = offset;
		this.limit = limit;
	}
}
