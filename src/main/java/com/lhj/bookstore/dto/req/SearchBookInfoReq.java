package com.lhj.bookstore.dto.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // @ModelAttribute로 값을 받기 위해 setter 적용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchBookInfoReq {
	
	@ApiModelProperty(value = "검색 키워드(도서명 or 저자명에 포함 되는 키워드)", required = false)
	private String keyword;
	
	@ApiModelProperty(value = "도서구분", required = false, allowableValues = "T001, T002, T003")
	private String type;
	
	@ApiModelProperty(value = "페이지 번호: 1", required = true)
	private Integer offset = 1;
	
	@ApiModelProperty(value = "페이지 사이즈: 10", required = true)
	private Integer limit = 10;
	
	@Builder
	public SearchBookInfoReq(String keyword, String type, Integer offset, Integer limit) {
		this.keyword = keyword;
		this.type = type;
		this.offset = offset;
		this.limit = limit;
	}
}
