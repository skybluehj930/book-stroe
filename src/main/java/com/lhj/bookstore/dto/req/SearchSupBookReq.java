package com.lhj.bookstore.dto.req;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // @ModelAttribute로 값을 받기 위해 setter 적용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchSupBookReq {
	
	@ApiModelProperty(value = "시작 일자: 2023-01-24", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startAt;
	
	@ApiModelProperty(value = "종료 일자: 2023-01-25", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endAt;

	@ApiModelProperty(value = "도서명: Hellow Java", required = false)
	private String title;
	
	@ApiModelProperty(value = "도서구분", required = false, allowableValues = "T001, T002, T003")
	private String type;
	
	@ApiModelProperty(value = "저자: 홍길동", required = false)
	private String writer;
	
	@ApiModelProperty(value = "페이지 번호: 1", required = true)
	private Integer offset = 1;
	
	@ApiModelProperty(value = "페이지 사이즈: 10", required = true)
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
