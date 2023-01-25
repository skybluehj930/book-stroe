package com.lhj.bookstore.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // @ModelAttribute로 값을 받기 위해 setter 적용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchContSupBookReq {
	
	@ApiModelProperty(value = "상태코드", required = false, allowableValues = "A, B, C")
	private String stateCd;
	
	@ApiModelProperty(value = "도서명: Hellow Java", required = false)
	private String title;
	
	@ApiModelProperty(value = "도서구분", required = false, allowableValues = "T001, T002, T003")
	private String type;
	
	@ApiModelProperty(value = "저자: 홍길동", required = false)
	private String writer;
	
	@ApiModelProperty(value = "페이지 번호: 1", required = true)
	private Integer offset;
	
	@ApiModelProperty(value = "페이지 사이즈: 10", required = true)
	private Integer limit;
	
	@Builder
	public SearchContSupBookReq(
					String stateCd
					, String title
					, String type
					, String writer
					, Integer offset
					, Integer limit) {
		
		this.stateCd = stateCd;
		this.title = title;
		this.type = type;
		this.writer = writer;
		this.offset = offset;
		this.limit = limit;
	}
}
