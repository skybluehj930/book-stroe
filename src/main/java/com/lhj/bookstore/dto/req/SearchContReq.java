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
public class SearchContReq {
	
	@ApiModelProperty(value = "상태 코드", required = false, allowableValues = "A, B, C")
	private String stateCd;
	
	@ApiModelProperty(value = "시작 일자: 2023-01-24", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startAt;
	
	@ApiModelProperty(value = "종료 일자: 2023-01-25", required = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate endAt;
	
	@ApiModelProperty(value = "페이지 번호: 1", required = true)
	private Integer offset = 1;
	
	@ApiModelProperty(value = "페이지 사이즈: 10", required = true)
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
